
from time import sleep
import json
import time

while True:
    data = {
        "date": int(time.time()),
        "high": 456.25,
        "open": 172.0,
        "close": 187.1,
        "low": 11.0,
        "volume": 745896.82
    }

    with open('/home/abm/MEGA/myprojects/ats/medium/data/responses/F@d@L.json', 'w') as f:
        f.write(json.dumps(data))
    print('data generated')
    sleep(0.1)