

from alphavantage import AlphaVantage
import json
import pathlib

class Management:
    stocks = [
        'FB',
        'V',
        'F'
    ]

    def historyData(self, symbol=None, start=None, end=None, interval=None):
        if(symbol != None):
            self.writeCandilesToFile(symbol, AlphaVantage().getData(symbol, start, end, interval))
            return
        for symbol in self.stocks:
            self.writeCandilesToFile(symbol, AlphaVantage().getData(symbol, start, end, interval))

    
    def writeCandilesToFile(self, symbol, candiles):
        # print(symbol, candiles[0].date, candiles[0].close)
        data = []
        for c in candiles: 
            data.append(c.toJSON())
        pathlib.Path('/home/abm/MEGA/myprojects/ats/medium/data').mkdir(exist_ok=True)
        f = open('/home/abm/MEGA/myprojects/ats/medium/data/'+symbol+'.json', 'w')
        f.write(json.dumps(data))
        f.close()

