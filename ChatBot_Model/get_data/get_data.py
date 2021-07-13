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

def tuition_fee(masv,pw):    
    login_data = {
        'maSV': masv,
        'pw': pw
    }   

    url = "http://daotao.ute.udn.vn/svlogin.asp"
    s = requests.Session()
    r = s.post(url, data=login_data)

    c = s.cookies.get_dict()
    key = list(c.keys())
    value = list(c.values())

    r.encoding = 'utf-8'
    soup = BeautifulSoup(r.content, 'lxml')

    cookie = key[0] + "=" + value[0]

    url = "http://daotao.ute.udn.vn/hpstatus.asp"

    r = requests.post(url, headers={'Cookie': cookie})
    r.encoding = 'utf-8'
    soup = BeautifulSoup(r.content, 'lxml')
    table = pd.read_html(soup.prettify())[3]
    table = table.rename(columns = {0 :'name', 1 : 'value' })

    return table