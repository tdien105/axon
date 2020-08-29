import os
import re,json, requests
from flask import request, Flask

# global vars
headers = {'content-type': 'application/json'}
app = Flask(__name__)
api_url = os.environ['api_url']
### end global vars

@app.route("/ping", methods=["GET"])
def healthcheck():
  return "pong"

@app.route("/", methods=["POST"])
def parsejson():
  link = request.json["json_link"]
  res = requests.get(link)

  if res.status_code == 200:
    lines = res.text.splitlines() # search line by line for more accurate
    for index,line in enumerate(lines):
      d = {'text': re.sub('[^a-zA-Z0-9 \n\.]', '', line), 'model': 'en'} #remove all special characters before extracting names
      try:
        res = requests.post(api_url, data=json.dumps(d), headers=headers)
      except:
        return "Error occur when connecting to backend api"

      if res.status_code == 200:
        r = res.json()
        for i in r:
          n = i['text']
          if i['type'] == "PERSON":
            line = line.replace(n,'___') # remove name from current line
      else:
        return "Error occur when connecting to backend api"
      lines[index] = line # update back to origin text
    
    result = json.loads('\n'.join(lines))
    result['AXON'] = "true"
    return result
  return "Could not fetch json files"

if __name__=='__main__':
    app.run(host= '0.0.0.0')