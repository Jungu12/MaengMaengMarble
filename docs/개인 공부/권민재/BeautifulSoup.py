from bs4 import BeautifulSoup
# bs4 사용방법
'''
powershell에서 pip install BeautifulSoup4 작성
'''
html_code = "<html><body><h1>안녕하세요!</h1></body></html>"

soup = BeautifulSoup(html_code, 'html.parser')

print("h1 태그의 텍스트:", soup.h1.string)