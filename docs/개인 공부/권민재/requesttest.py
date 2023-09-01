import requests

url = "https://www.google.com"

# 해당 url에 요청
response = requests.get(url)

# print("상태 코드:", response.status_code)

# print("페이지 내용:", response.text)

# params = {'param1': 'value1', 'param2': 'value'}
# res = requests.get(url, params=params)
# print(res.url)

# data = {'param1': 'value1', 'param2': 'value'}
# res = requests.post(url, data=data)
# print(res.url)

import requests, json
data = {'outer': {'inner': 'value'}}
res = requests.post(url, data=json.dumps(data))
print(res)