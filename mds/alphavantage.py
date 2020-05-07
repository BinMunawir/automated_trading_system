from alpha_vantage.timeseries import TimeSeries
from candile import Candile


class AlphaVantage:
    def getData(self, symbol, start=None, end=None, interval=None):
        ts = TimeSeries(key='TYBF948GTKE4QNN2')

        if(interval is None):
            return self.formatData(ts.get_daily(symbol, 'full'), start, end)
        return self.formatData(ts.get_intraday(symbol, interval, 'full'), start, end)

    def formatData(self, data, startDate=None, endedDate=None):
        formattedData = []
        for k, v in data[0].items():
            if(startDate != None and int(str(startDate).replace('-', '')) > int(str(k).replace('-', '')[:9])):
                break
            if(endedDate != None and int(str(endedDate).replace('-', '')) < int(str(k).replace('-', '')[:9])):
                continue
            formattedData.append(Candile(k, v['2. high'], v['1. open'], v['4. close'], v['3. low'], v['5. volume']))
        return formattedData

