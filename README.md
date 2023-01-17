# user-authentication-system
User authentication, authorization and verification API's made with Spring Boot, Spring JPA, h2 database and Rabbit MQ with all the basic features and more !!



### Technologies and Framework
* Spring Boot
* Spring Security
* Spring Hibernate
* H2 Database
* Rabbit MQ

## File Structure
![image](https://user-images.githubusercontent.com/60911166/212850913-599b4c58-1b6a-4ced-a844-c1e0c5f6e374.png)

## Basic Command
```
https://github.com/ferrero-rocher/user-authentication-system
```

## Docker Build
docker-compose up //in the root directory( It might take a while for the container to spin up for the first time)

![image](https://user-images.githubusercontent.com/60911166/212852247-8ebcdfe1-9d94-4f17-a58f-03c2492467c3.png)

![image](https://user-images.githubusercontent.com/60911166/212851898-4571fb8f-e8a8-408e-bcc6-a46fd11ca95f.png)


## API - Documentation
Basepath - htttp://localhost:9001/

|Routes|Method|Comment|Description| 
|---|---|---|---|
|api/v1/register| POST | Register a new user | Initially when a user registers he will have to verify himself with the  token generated via this call |
|api/v1/verifyRegistration?token=${value}| GET |Verify a registered User |User will have 10 minutes to verify himself by using the value of token generated in previous call|
|api/v1/resendVerificationURL?userid=${value}| GET| Resend token for Verification| Resends the verification token|
|api/v1/forgotPassword?email=${value}| GET | Sends reset password token for given email ID | Send a new verification token in response which has to be passed as query paramater in subsequent call|
|api/v1/savePassword?token=${value}| POST | Saves new Password | User can save their new  password via this URL |
|api/v1/user/changePassword| POST| Change current password | User can change thier password but they have to be authenticated in the system|
|api/v1/activity| POST | Insert Activity | Inserts custom activity log| URL is used to insert custom activity logs|  

```
For all the endpoints to work properly you have to pass required paramaters and payload  else you'll be thrown a custom exception with proper error code and response
```


## Request Payload

The tool used here is ***POSTMAN*** to test all the endpoints

:round_pushpin:  <ins>**POST   api/v1/register**</ins> 
```
{
    "firstname":"lol",
    "lastname":"Demo",
    "username":"user",
    "email":"xyz@gmail.com",
    "password":"122sff44646"
}
```
```
All fields are compulsory
```

![image](https://user-images.githubusercontent.com/60911166/212923941-5d1a141a-f71e-4b06-8ee7-0ce10dffa254.png)

_________________________________________ 

:round_pushpin:  <ins>**GET   api/v1/verifyRegistration?token=${value}** </ins>
```
The response of previous call has to be passed as a query parameter
```

![image](https://user-images.githubusercontent.com/60911166/212924770-d1fa5e95-9b60-491f-91c7-762617287fad.png)


_________________________________________ 

:round_pushpin:  <ins>**GET   api/v1/resendVerificationURL?userid=${value}**</ins>
```
Only a registered user will receive a new verification token
```

![image](https://user-images.githubusercontent.com/60911166/212925595-93267076-346d-4e0f-9376-d152a5b1eb9f.png)


_________________________________________ 

:round_pushpin:  <ins>**GET   api/v1/forgotPassword?email=${value}**</ins>
```
Only a registered user who is verified will receive a new verification token to change the password
```
![image](https://user-images.githubusercontent.com/60911166/212935637-e1e76519-dceb-409c-9922-e60484934224.png)


_________________________________________ 

:round_pushpin:  <ins>**POST   api/v1/savePassword?token=${value}**</ins>
```
Token from previous call has to passed as query paramater and new password field as payload  
```
Payload
```
{
    "newPassword":"Shaun@123"
}
```
![image](https://user-images.githubusercontent.com/60911166/212938906-ff83e787-efa5-4de9-b0ba-e8cafb8343e5.png)


_________________________________________ 

:round_pushpin:  <ins>**POST   api/v1/user/changePassword**</ins>
```
This endpoint uses basic authentication to authenticate the user and then only users will be allowed to acesss this endpoint 
```
Payload
```
{
    "email":"xyz@gmail.com",
    "oldPassword":"Shaun@123",
    "newPassword":"abfakbfkan"

}
```
![image](https://user-images.githubusercontent.com/60911166/212940208-3fb1da5c-c7b2-41cb-b4d1-45a1a371dcac.png)


_________________________________________ 


:round_pushpin:  <ins>**POST   api/v1/activity**</ins>
```
User can post custom activity to the Rabbit MQ and then access URL:- localhost:15672 with guest as username and password to retrieve all the user activity logs and custom logs
```
![image](https://user-images.githubusercontent.com/60911166/212941644-e936bbfc-0add-4e3b-86e2-8551f9434535.png)
_________________________________________ 

![image](https://user-images.githubusercontent.com/60911166/212942457-670615be-55c5-46ab-8553-310951cab9cd.png)

# **WHATS NEXT?**
* Instead of sending verification URL in the response payload , will make use of TWILIO to send it via email









