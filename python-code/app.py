# save this as app.py
from sklearn.naive_bayes import MultinomialNB
from sklearn.feature_extraction.text import CountVectorizer
from flask import Flask, request, jsonify
from sklearn.model_selection import train_test_split
import numpy as np
import pandas as pd
import nltk
from nltk.corpus import stopwords

app = Flask(__name__)
mnb = MultinomialNB()
vec = CountVectorizer(stop_words=stopwords.words('english'))

def buildModel():
    df = pd.read_excel('spam.xlsx')
    df['Class'] = df.Class.map({'ham': 0, 'spam': 1})
    numpy_array = df.to_numpy()
    Y = numpy_array[:, 0]
    X = numpy_array[:, 1]
    Y = Y.astype('int')
    X = [str(item) for item in X]
    vec.fit(X)
    X_transformed = vec.transform(X)
    X = X_transformed.toarray()
    pd.DataFrame(X, columns=vec.get_feature_names())
    mnb.fit(X, Y)

@app.route("/sms", methods=['POST'])
def sms():
    json_data = request.json
    sms = json_data['sms']
    prediction = predict(sms)
    responseJson = {}
    responseJson['class'] = 'spam' if prediction[0] == True else 'non-spam'
    return jsonify(responseJson)


def predict(sms):
    test_sms = [str(sms)]
    prediction = mnb.predict(vec.transform(test_sms))
    return prediction
buildModel()