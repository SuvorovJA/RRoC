обращение без токена
```bash
$ curl  'localhost:8080/citizens/5'
```
```json
{"error":"unauthorized","error_description":"Full authentication is required to access this resource"}
```


запрос токена у независимого сервера авторизации
```bash
$ curl -u clientId:secret -X POST localhost:9000/oauth/token\?grant_type=password\&username=faro\&password=faro-password
```
```json
{
  "access_token" : "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTQ3OTQ5MzgsInVzZXJfbmFtZSI6ImZhcm8iLCJhdXRob3JpdGllcyI6WyJST0xFX01PRElGSUNBVElPTiIsIlJPTEVfUkVBRE9OTFkiXSwianRpIjoiNzRmYzFiNzUtYjVlMS00NzgyLWI4OTYtZWQwY2UwYTc5OGIwIiwiY2xpZW50X2lkIjoiY2xpZW50SWQiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.0CROnhJTRdF3ARVQuE0QdhT8VVQk5HsL3fyqIjN5wXzzKa_aaO9piTiLOJCwb01v2R0vQLkS8cqo2Kbwt75cD7zTXDdODmblFTNfG587KxQ4_kZuxWdHO02x0K8_4syxOxrYvYgeoa0RqhMoidLaA8k1FRBJJYGYPN8zb980BnEKjYYbBl9F5DmgAWFw_459MaVaUV8fskYduSK_sQRAp8UepEgQaDQ_aiNTPrUK_WxF03FjU1tCAZ0frItvnyP1Q5pPA9BJkWu_5xndGW4qTYZWLgiSFOGyCA10haNhJ8_fvoI4pvkoCeW4mmNO3M-CGVhrDYZwJ7rY3r261gZZJw",
  "token_type" : "bearer",
  "refresh_token" : "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJmYXJvIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImF0aSI6Ijc0ZmMxYjc1LWI1ZTEtNDc4Mi1iODk2LWVkMGNlMGE3OThiMCIsImV4cCI6MTU1NzM4NjYzOCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9NT0RJRklDQVRJT04iLCJST0xFX1JFQURPTkxZIl0sImp0aSI6Ijc4ZTU4OWYwLWY1OWMtNGY5MC1hMTkxLTBjMDFjODJkYzk3MSIsImNsaWVudF9pZCI6ImNsaWVudElkIn0.3-ZPrqTDY3wHT0rRy9faByDeV5oalbLDfqsftuSouMO1vOfjrTSUq2GZ9MUY0lcezgI_4kniOKsAUTylRmU16SEWkdjiamFESbIBcNt3kJ3orgt6gD4NjWIAuume71xgZKWhg7VMqFWZmydZHbR5efHTvC8NGyYiMu6Q_Y6dFEqbRogTzTrYlRup7EBD6bGE8tVFB6x81254vdyeMAfQBnS7Lzbt_hyY3ImFf1dfWAxE6zFT_fx0Rj7kT-KLgSYyAGSa4lrN9Z3S3IwvBViQEC0KOYoSxGSSlbiBxUGzxunzYx1bUM-2b1aM7pdNcukVsSgI3qncE-GbYqSRZTsb1g",
  "expires_in" : 299,
  "scope" : "read write",
  "jti" : "74fc1b75-b5e1-4782-b896-ed0ce0a798b0"
}
```


обращение с токеном
```bash
$ curl localhost:8080/citizens/10 -H "Authorization: bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTQ3OTQ5MzgsInVzZXJfbmFtZSI6ImZhcm8iLCJhdXRob3JpdGllcyI6WyJST0xFX01PRElGSUNBVElPTiIsIlJPTEVfUkVBRE9OTFkiXSwianRpIjoiNzRmYzFiNzUtYjVlMS00NzgyLWI4OTYtZWQwY2UwYTc5OGIwIiwiY2xpZW50X2lkIjoiY2xpZW50SWQiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.0CROnhJTRdF3ARVQuE0QdhT8VVQk5HsL3fyqIjN5wXzzKa_aaO9piTiLOJCwb01v2R0vQLkS8cqo2Kbwt75cD7zTXDdODmblFTNfG587KxQ4_kZuxWdHO02x0K8_4syxOxrYvYgeoa0RqhMoidLaA8k1FRBJJYGYPN8zb980BnEKjYYbBl9F5DmgAWFw_459MaVaUV8fskYduSK_sQRAp8UepEgQaDQ_aiNTPrUK_WxF03FjU1tCAZ0frItvnyP1Q5pPA9BJkWu_5xndGW4qTYZWLgiSFOGyCA10haNhJ8_fvoI4pvkoCeW4mmNO3M-CGVhrDYZwJ7rY3r261gZZJw"
```
```json
{
  "id" : 10,
  "fullName" : "Brooks-Barton",
  "dob" : "1971-03-14",
  "address" : "8421 Pine View Parkway",
  "dulnumber" : "7316953795"
}
```


обращение с токеном за многостраничным списком
```bash
$ curl -v 'localhost:8080/citizens?page=0&size=5' -H "Accept: application/json" -H "Content-type: application/json" -H "Authorization: bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTQ3OTUzMTAsInVzZXJfbmFtZSI6ImZhcm8iLCJhdXRob3JpdGllcyI6WyJST0xFX01PRElGSUNBVElPTiIsIlJPTEVfUkVBRE9OTFkiXSwianRpIjoiNzI4ZjA1NmQtOTQxZS00NjdhLThlMmQtNGY3OTljNjgzOTk1IiwiY2xpZW50X2lkIjoiY2xpZW50SWQiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.EXcdjsCRXwxvnU2nFOGz4zNE99-oafM9fj94US3YQY-nH-pyhbrLM22kpYUbhRx6JuP2wNUOaNMAVEWrChUhIgvjcK5K6Me9_EKyPkHkW5gFYaCPQiOdHlaPtey0YK58MtrRvPoaf6o1bpLV8aiHLYOBeGoS88V5tBfhX8I6nB8WRbOUhzLNgVO_e4YiDdMRJQ01aSMWoJLeNTdD-gYuV0CqHF3TdLu0_b8WRxF1EjwDIG0blN1Wx81SqPKbzIhGYktRDotTTb6YeaEPhwgg8KaN4hqhEORWlCLQFFrVBgmrHdkPyAZivCVZusUoz13l2O6JHC6hNxxOk-45dWXUCw"
... HTTP/1.1 200 ...
```
```json
{
  "content" : [ {
    "id" : 1,
    "fullName" : "Флетчер-Крёйгер",
    "dob" : "1970-11-12",
    "address" : "1869 Di Loreto Center",
    "dulnumber" : "8274773873"
  }, {
    "id" : 2,
    "fullName" : "Jenee-Crooks",
    "dob" : "1987-04-10",
    "address" : "19 Sachtjen Drive",
    "dulnumber" : "22173309533"
  }, {
    "id" : 3,
    "fullName" : "Pete-Hagenes",
    "dob" : "1973-06-16",
    "address" : "4440 Laurel Lane",
    "dulnumber" : "371474"
  }, {
    "id" : 4,
    "fullName" : "King-Cruickshank",
    "dob" : "1990-02-17",
    "address" : "968 Towne Parkway",
    "dulnumber" : "121436496169"
  }, {
    "id" : 5,
    "fullName" : "Mickey-Treutel",
    "dob" : "1996-03-19",
    "address" : "90 Bunker Hill Terrace",
    "dulnumber" : "205343909704"
  } ],
  "pageable" : {
    "sort" : {
      "sorted" : false,
      "unsorted" : true,
      "empty" : true
    },
    "pageSize" : 5,
    "pageNumber" : 0,
    "offset" : 0,
    "paged" : true,
    "unpaged" : false
  },
  "totalPages" : 20,
  "totalElements" : 99,
  "last" : false,
  "first" : true,
  "number" : 0,
  "sort" : {
    "sorted" : false,
    "unsorted" : true,
    "empty" : true
  },
  "numberOfElements" : 5,
  "size" : 5,
  "empty" : false
}
```


обращение с истёкшим токеном
```bash
$ curl -v 'localhost:8080/citizens/5' -X DELETE -H "Accept: application/json" -H "Content-type: application/json" -H "Authorization: bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTQ3OTUzMTAsInVzZXJfbmFtZSI6ImZhcm8iLCJhdXRob3JpdGllcyI6WyJST0xFX01PRElGSUNBVElPTiIsIlJPTEVfUkVBRE9OTFkiXSwianRpIjoiNzI4ZjA1NmQtOTQxZS00NjdhLThlMmQtNGY3OTljNjgzOTk1IiwiY2xpZW50X2lkIjoiY2xpZW50SWQiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.EXcdjsCRXwxvnU2nFOGz4zNE99-oafM9fj94US3YQY-nH-pyhbrLM22kpYUbhRx6JuP2wNUOaNMAVEWrChUhIgvjcK5K6Me9_EKyPkHkW5gFYaCPQiOdHlaPtey0YK58MtrRvPoaf6o1bpLV8aiHLYOBeGoS88V5tBfhX8I6nB8WRbOUhzLNgVO_e4YiDdMRJQ01aSMWoJLeNTdD-gYuV0CqHF3TdLu0_b8WRxF1EjwDIG0blN1Wx81SqPKbzIhGYktRDotTTb6YeaEPhwgg8KaN4hqhEORWlCLQFFrVBgmrHdkPyAZivCVZusUoz13l2O6JHC6hNxxOk-45dWXUCw"
```
```json
{"error":"invalid_token","error_description":"Access token expired: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTQ3OTUzMTAsInVzZXJfbmFtZSI6ImZhcm8iLCJhdXRob3JpdGllcyI6WyJST0xFX01PRElGSUNBVElPTiIsIlJPTEVfUkVBRE9OTFkiXSwianRpIjoiNzI4ZjA1NmQtOTQxZS00NjdhLThlMmQtNGY3OTljNjgzOTk1IiwiY2xpZW50X2lkIjoiY2xpZW50SWQiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.EXcdjsCRXwxvnU2nFOGz4zNE99-oafM9fj94US3YQY-nH-pyhbrLM22kpYUbhRx6JuP2wNUOaNMAVEWrChUhIgvjcK5K6Me9_EKyPkHkW5gFYaCPQiOdHlaPtey0YK58MtrRvPoaf6o1bpLV8aiHLYOBeGoS88V5tBfhX8I6nB8WRbOUhzLNgVO_e4YiDdMRJQ01aSMWoJLeNTdD-gYuV0CqHF3TdLu0_b8WRxF1EjwDIG0blN1Wx81SqPKbzIhGYktRDotTTb6YeaEPhwgg8KaN4hqhEORWlCLQFFrVBgmrHdkPyAZivCVZusUoz13l2O6JHC6hNxxOk-45dWXUCw"}
```


запрос удаления
```bash
$ curl -v 'localhost:8080/citizens/5' -X DELETE -H "Accept: application/json" -H "Content-type: application/json" -H "Authorization: bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTQ3OTU2OTIsInVzZXJfbmFtZSI6ImZhcm8iLCJhdXRob3JpdGllcyI6WyJST0xFX01PRElGSUNBVElPTiIsIlJPTEVfUkVBRE9OTFkiXSwianRpIjoiZDI1NmRhYTUtY2U1OC00YzAxLWFkMWMtODM1Yjg5ZjgwZDFhIiwiY2xpZW50X2lkIjoiY2xpZW50SWQiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.v-xnD0NqpmwMD8neyY-t_X1CnDWVPM1s33HIAthmSCBgSinGGk5CHQIrrhu4i9dOxx2ArQEiCPlz2lEKsbyOHKl0fcUQfUrsy8ivmELn-Ja_lutdBQg40T-IUAZY3oV30DkwTxmMrU-yspYW44mVdAATljNweqXdc957KAZWXJJ-vXAvZfmGzLllCOBq915AEeUg1d09f5ma2eaznBPT2-CAIrBJk8r2J3Z_y6w2DSf6trOQZ5-dyCGKig8UWmSn12qTkPe86G-_wc2vD00L8Tnvfv9WX3i_p4rFH8Oygboc6k_XmZRSh5jN20jdWOsNFhC1wNnkEkQ1PTVXeNDj-Q"
... HTTP/1.1 200 ...
```


запрос к удалённому
```bash
$ curl -v 'localhost:8080/citizens/5' -X GET -H "Accept: application/json" -H "Content-type: application/json" -H "Authorization: bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTQ3OTU2OTIsInVzZXJfbmFtZSI6ImZhcm8iLCJhdXRob3JpdGllcyI6WyJST0xFX01PRElGSUNBVElPTiIsIlJPTEVfUkVBRE9OTFkiXSwianRpIjoiZDI1NmRhYTUtY2U1OC00YzAxLWFkMWMtODM1Yjg5ZjgwZDFhIiwiY2xpZW50X2lkIjoiY2xpZW50SWQiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.v-xnD0NqpmwMD8neyY-t_X1CnDWVPM1s33HIAthmSCBgSinGGk5CHQIrrhu4i9dOxx2ArQEiCPlz2lEKsbyOHKl0fcUQfUrsy8ivmELn-Ja_lutdBQg40T-IUAZY3oV30DkwTxmMrU-yspYW44mVdAATljNweqXdc957KAZWXJJ-vXAvZfmGzLllCOBq915AEeUg1d09f5ma2eaznBPT2-CAIrBJk8r2J3Z_y6w2DSf6trOQZ5-dyCGKig8UWmSn12qTkPe86G-_wc2vD00L8Tnvfv9WX3i_p4rFH8Oygboc6k_XmZRSh5jN20jdWOsNFhC1wNnkEkQ1PTVXeNDj-Q"
... HTTP/1.1 404 ...
```
