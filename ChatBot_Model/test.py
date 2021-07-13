import requests
from bs4 import BeautifulSoup
import pandas as pd

def svtkb(masv):

    url = "http://daotao.ute.udn.vn/svtkb.asp" + "?masv=" + masv

    r = requests.get(url)
    r.encoding = 'utf-8'
    soup = BeautifulSoup(r.content, 'lxml')
    try:
        table = pd.read_html(soup.prettify())[0]
        table = table.rename(columns = {0 :'course', 1 : 'name', 2 : 'day', 3 : 'from', 4 : 'to', 5 : 'teacher', 6 : 'room'})
        table.to_json(masv + '.json')
    except:
        table = []
    
    
    return table

masv = '1811505310243'
if len(svtkb(masv)) == 0:
    print('Không có thời khóa biểu của sinh viên này')
else:
    _ = svtkb(masv)
    print(_)
    
