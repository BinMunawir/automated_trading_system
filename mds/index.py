
from candile import Candile
from alphavantage import AlphaVantage
from management import Management
import time
from time import sleep


# t = time.time()
# t = time.time() - t
# print(t)

# candile = Candile()
# print(candile.toJSON())

# data = AlphaVantage().getData('AAPL', '2020-05-01', '2020-05-05', interval='1min')
# print(len(data))

Management().historyData(interval='1min')
