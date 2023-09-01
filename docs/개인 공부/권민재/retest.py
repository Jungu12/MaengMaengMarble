import re

text = "안녕하세요! 저는 홍길동이고,\
    이메일 주소는 hoggildong@example.com 입니다."
    
pattern = r"[a-zA-Z0-9+.+-]+@[a-zA-Z0-9-]+\
    \.[a-zA-Z0-9-.]+"
    
emails1 = re.findall(pattern, text)
emails2 = re.match(pattern, text)
emails3 = re.search(pattern, text)
emails4 = re.split(pattern, text)
emails5 = re.sub("저", "강", text)
print(emails1)
print(emails2)
print(emails3)
print(emails4)
print(emails5)