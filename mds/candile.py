
import json


class Candile:
    def __init__(self, date= '', high= '', open= '', close= '', low= '', volume= ''):
        self.date  = date
        self.high  = high
        self.open  = open
        self.close  = close
        self.low  = low
        self.volume  = volume
    def toJSON(self) -> str:
        return json.dumps({self.date: {
            'high': self.high,
            'open': self.open,
            'close': self.close,
            'low': self.low,
            'volume': self.volume
        }})
