# capstone
To start the flask web service:<br>
<i>cd python-code</i><br>
<i>flask run</i><br><br>

To make the flask web service accessible publicly:<br>
<i>npx localtunnel --port 5000 --subdomain agbi</i><br>
5000 is the default port on which flask runs.<br>
<br>
Download the apk file and install it.<br>
Set it as default SMS App.<br>

The url used by the android code to communicate with the flask web service is : <br>
https://agbi.loca.lt/sms<br>
A POST request is sent with body : <br>
<i>{"sms" : "This is a sample text"}</i><br><br>
Sample response: <i>{"class": "spam"}</i><br>
