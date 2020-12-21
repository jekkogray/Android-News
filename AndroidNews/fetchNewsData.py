import requests
url = ('http://newsapi.org/v2/top-headlines?'
               'country=us&' 'apiKey=7faafa8f7be94d6c863757dde0c1ddc3')
response = requests.get(url).json()
fileName = './app/sampledata/NewsTopHeadlines.json'
f=open(fileName, "w+")
if not f.write(str(response)):
    print(response)
f.close()
#7faafa8f7be94d6c863757dde0c1ddc3
