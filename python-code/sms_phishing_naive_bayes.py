# Import packages
from sklearn.model_selection import train_test_split
from sklearn.metrics import confusion_matrix
import numpy as np
import pandas as pd

#importing the dataset
df = pd.read_excel('spam.xlsx')

df['Class']=df.Class.map({'ham':0, 'spam':1})

numpy_array = df.to_numpy()
Y=numpy_array[:,0]
X=numpy_array[:,1]
Y=Y.astype('int')

from sklearn.feature_extraction.text import CountVectorizer
vec = CountVectorizer()


from sklearn.model_selection import train_test_split
X_train, X_test, Y_train, Y_test = train_test_split(X,Y, test_size =0.25, random_state = 0)
X_train = [str (item) for item in X_train]
vec.fit(X_train)

X_train_transformed=vec.transform(X_train)

X_train=X_train_transformed.toarray()
pd.DataFrame(X_train, columns=vec.get_feature_names())

"""# Multinomial Naive Bayes"""

from sklearn.naive_bayes import MultinomialNB
mnb=MultinomialNB()
mnb.fit(X_train,Y_train)
test_sms =[str ("This is a sample SMS")]
prediction = mnb.predict(vec.transform(test_sms))
print(prediction)

