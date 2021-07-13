from typing import Any, Text, Dict, List
from rasa_sdk.events import FollowupAction
from rasa_sdk import Action, Tracker
from rasa_sdk.executor import CollectingDispatcher
import random
# from utility import *
import os
import dateparser
from pyvi import ViTokenizer
import re
import pandas as pd
from get_data.get_data import svtkb, tuition_fee
from rasa_sdk.events import SlotSet


# mssv = ''
regrex = r'([0-9]{13})'
 
tkb_filename = 'getdata/'


class ActionShowSchedule(Action):
    def name(self) -> Text:
        return "action_show_schedule"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        message = tracker.latest_message.get('text')
        
        senderid = tracker.sender_id
        print('#### sender_ID ', senderid)

        masv = senderid
        tkb_filename = masv + '.json'
        print(tkb_filename)
        
        if os.path.isfile(tkb_filename) != True:
            if len(svtkb(masv)) == 0:
                print(len(svtkb(masv)))
                dispatcher.utter_message(text='Không có thời khóa biểu của sinh viên này')
                return []
            else:
                _ = svtkb(masv)
                dispatcher.utter_message(text='Đã tải thời khoá biểu')
    
        tkb = pd.read_json(tkb_filename)
        tokens = ViTokenizer.tokenize(message)
        print('tokens', tokens)
        for token in tokens.split(' '):
            if token.find('_')>0 and dateparser.parse(token.replace('_',' '), languages=['vi']):
                dates = dateparser.parse(token.replace('_',' '), languages=['vi']).weekday()
                print('date', dates)
                list_course = tkb[tkb['day'] == dates+2]
                response = ''
                if len(list_course):
                    for _, row in list_course.iterrows():
                        response += "Bạn có môn học {} từ tiết {} đến tiết {} ở phòng {} \n".format(row['name'], row['from'], row['to'], row['room'])
                else:
                    response = 'Bạn không có lịch học'
            else:
                response = 'Bạn muốn hỏi lịch học ngày nào'
        dispatcher.utter_message(text=response)
       
        return []


# class ActionSessionId(Action):

#     def name(self) -> Text:
#         return "action_sesion_id"

#     def run(self, dispatcher: CollectingDispatcher,
#             tracker: Tracker,
#             domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

#         conversation_id = tracker.sender_id


#         message = tracker.latest_message.get('text')
        
#         masv = tracker.get_slot("masv") 
#         pw = tracker.get_slot("pw")
        
#         if os.path.isfile(tkb_filename):
           
#             response = ''
#             if len(list_course):
#                 for _, row in list_course.iterrows():
#                     response += "Bạn có môn học {} từ tiết {} đến tiết {} ở phòng {} \n".format(row['name'], row['from'], row['to'], row['room'])
#             else:
#                 response = 'Bạn không có lịch học'

#             dispatcher.utter_message(text=response)
#         else:
#             mssv = re.search(regrex, message)            
#             try:
#                 masv = mssv[0]
#                 print('mssv : ', masv)
#                 _ = svtkb(masv)
#                 dispatcher.utter_message(text='Đã tải thời khoá biểu')
#                 return [SlotSet("masv", masv)]
#             except:
#                 dispatcher.utter_message(text='Vui lòng nhập MSSV')
#         return []