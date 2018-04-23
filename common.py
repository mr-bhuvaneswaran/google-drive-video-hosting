import datetime

import gspread
from apiclient import discovery
from httplib2 import Http
from oauth2client import file,client,tools

SCOPES = ['https://www.googleapis.com/auth/spreadsheets','https://www.googleapis.com/auth/drive']
#client_secret.json is downloaded from google developer console
CLIENT_SECRET = 'client_secret.json'

#storage json is stored in local directory
store = file.Storage('storage.json')

cred = store.get()

if not cred or cred.invalid:
    flow = client.flow_from_clientsecrets(CLIENT_SECRET,SCOPES)
    cred = tools.run_flow(flow,store)

SERVICE = discovery.build('drive', 'v2',http=cred.authorize(Http()))
#replace the folder name with drive folder id 
results = SERVICE.files().list(q = "'<folder name>' in parents",spaces='drive',
                                          fields='nextPageToken, items(id, createdDate, webContentLink)',pageToken=None).execute()
items = results.get('items', [])

gc = gspread.authorize(cred)
#status is the google spreadsheet name
rec = gc.open('status').sheet1
result = datetime.datetime.strptime(rec.cell(1,4,'FORMATTED_VALUE').value, '%Y-%m-%dT%H:%M:%S')
update = result
str_date = ""
if not items:
    print('No files found.')
else:
    for item in items:
        date = item['createdDate']
        date = datetime.datetime.strptime(date[:len(date)-5], '%Y-%m-%dT%H:%M:%S')
        if date > result:
            rec.append_row([item['createdDate'],item['id'],item['webContentLink']])
            if update < date:
                update = date
                str_date = item['createdDate']
rec.update_cell(1,4,str(str_date[:len(str_date)-5]))
