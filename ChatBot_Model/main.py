from tkinter import font
import requests
import tkinter
import ast

root = tkinter.Tk()
root.geometry("670x800")
root.title("Rasa Chatbot")
root.resizable(0,0)
text = tkinter.StringVar()
conversation = ["           Welcome to Rasa Chatbot             \n".center(120)]

text.set("\n".join(conversation))

label = tkinter.Label(root, textvariable=text, height=40, width=60, justify=tkinter.LEFT,anchor='nw', font={"family":"Arial Black", "size":20})

label.grid(row=0,column=0)

entry = tkinter.Entry(root,width=72)
entry.grid(row=5,column=0)

def ReadText():
    message = entry.get()
    conversation.append("User: " + message)
    text.set("\n".join(conversation))
    label.update()

    url = 'http://localhost:5005/webhooks/rest/webhook'

    myobj ={
        "message": message,
        "sender": "1811505310221"
    }

    entry.delete(0,'end')
    x = requests.post(url,json=myobj)
    ast.literal_eval(x.text)

    print( ast.literal_eval(x.text)[-1])

    reply = ""
    if len(ast.literal_eval(x.text)[0]["text"].split(" "))//9 + 1:
        for i in range(len(ast.literal_eval(x.text)[0]["text"].split(" ")) // 9 + 1):
            reply += " ".join(ast.literal_eval(x.text)[0]["text"].split(" ")[9 * (i - 1):9 * (i -1) + 9])
    else:
        reply = ast.literal_eval(x.text)[0]["text"]
    conversation.append("Rasa: " + reply)

    print( [list(i.keys())[1] for i in ast.literal_eval(x.text)])
    if 'image' in [list(i.keys())[1] for  i in ast.literal_eval(x.text)]:
        def OpenImage():
            import webbrowser
            webbrowser.open(ast.literal_eval(x.text)[1]["image"].replace("\\",""))
        conversation.append("Rasa: " + ast.literal_eval(x.text)[1]["image"].replace("\\",""))
        b = tkinter.Button(root, text = "Open Image",command=OpenImage).pack(...)
    text.set("\n".join(conversation)) 

button = tkinter.Button(root, text="Send", command=ReadText)
button.grid(row=5, column=40)
root.configure(background='orange')
label.configure(background='orange')

root.mainloop()




