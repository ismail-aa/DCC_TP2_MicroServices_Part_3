# Security Service with Users Stored in The Database

## Notes :

- JWT authentication
- Passwords are encoded using BCrypt
- Access tokens expire in 2 mins. Refresh tokens expire in 15 mins

# Tests & Results

## Post Method for user1 : http://localhost:9090/login

<img width="937" height="581" alt="image" src="https://github.com/user-attachments/assets/a562773a-3dc2-4862-9d7e-f8a8368b8b1d" />


## Post Method for user1 after pasting the refresh token : http://localhost:9090/refresh

<img width="942" height="588" alt="image" src="https://github.com/user-attachments/assets/76d423b4-28f4-4a83-bcf7-8d8601dcdfb2" />


## Post Method for admin1 : http://localhost:9090/login

<img width="942" height="579" alt="image" src="https://github.com/user-attachments/assets/36875cbb-0632-4055-8ee9-1fa9818ebb19" />


## Post Method for admin1 after pasting the refresh token : http://localhost:9090/refresh

<img width="941" height="576" alt="image" src="https://github.com/user-attachments/assets/347c9eb2-0b48-40e6-ae27-392e271f11a2" />


## Post Method for admin2 and user2 that don't exist

<img width="943" height="424" alt="image" src="https://github.com/user-attachments/assets/9f743942-6404-45c5-be2b-543986df981d" />

### 

<img width="945" height="423" alt="image" src="https://github.com/user-attachments/assets/5b689413-b3c3-4d37-be28-27cc6c5ccdbd" />


## The Users Created in the Database " security_db "


<img width="727" height="108" alt="image" src="https://github.com/user-attachments/assets/b2a44076-b644-4142-a34c-53b9251ec3b5" />





