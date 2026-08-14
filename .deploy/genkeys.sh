openssl genrsa -out private-key.pem 2048
openssl rsa -pubout -in private-key.pem -out public-key.pem