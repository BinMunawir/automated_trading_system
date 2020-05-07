

from alphavantage import AlphaVantage
import json
import pathlib

class Management:
    stocks = [
        'AAPL'
    ]

    def historyData(self, symbol=None, start=None, end=None, interval=None):
        if(symbol != None):
            self.writeCandilesToFile(symbol, AlphaVantage().getData(symbol, start, end, interval))
            return
        for symbol in self.stocks:
            self.writeCandilesToFile(symbol, AlphaVantage().getData(symbol, start, end, interval))

    
    def writeCandilesToFile(self, symbol, candiles):
        data = []
        for c in candiles: 
            data.append(c.toJSON())
        pathlib.Path('../data').mkdir(exist_ok=True)
        f = open('../data/'+symbol+'.json', 'w')
        f.write(json.dumps(data))
        f.close()

