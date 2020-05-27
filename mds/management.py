

from alphavantage import AlphaVantage
import json
import os
from time import sleep
import datetime

class Management:

    def receiveRequests(self):
        path = '/home/abm/MEGA/myprojects/ats/medium/data/requests'
        while True:
            for (dirpath, dirnames, filenames) in os.walk(path):
                for f in filenames:
                    sleep(0.1)
                    print(f)
                    content = ''
                    with open(path+'/'+f, 'r') as file:
                        content = file.read()
                    try:
                        request = json.loads(content)
                    except:
                        print('err')
                        continue
                    os.remove(path+'/'+f)
                    print("request: " ,request)
                    self.provideData(f, request)

    def provideData(self, id, request):
        if 'interval' in request:
            request['interval'] = None if request['interval'] == 'd' else request['interval']
        isList = True if 'start' in request else False

        if isList:
            request['start'] = None if request['start'] == 0 else str(datetime.datetime.fromtimestamp(request['start']/1000))[0:10]
            request['end'] = None if request['end'] == 0 else str(datetime.datetime.fromtimestamp(request['end']/1000))[0:10]
            data = AlphaVantage().getData(request['symbol'], request['interval'], request['start'], request['end'])
        else:
            data = AlphaVantage().getData(request['symbol'], request['interval'])
        data = data if isList else data[0]

        self.serveResponse(id, isList, data)
            

    
    def serveResponse(self, id, isList, response):
        path = '/home/abm/MEGA/myprojects/ats/medium/data/responses/'+id+'.json'
        data = ''
        if isList:
            for r in response:
                data += r.toJSON()
        else:
            data += response.toJSON()
        
        with open(path, 'w') as f:
            f.write(data)

