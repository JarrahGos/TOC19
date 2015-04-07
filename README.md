TOC19
=====

TOC Repositry for 19 Div
---

### Scope

TOC19 is a program designed for customer centered shopping in a small store environment. It is based on the idea that each customer will be a member of the organisation, and will have an account which can be charged later. 

As such, TOC19 allows the managers of the store to allow the store to run itself, simpily collecting the bills at the end of each cycle and then distributing them to the users. 

### Method

* TOC19 uses both a user system and an admin system to achieve it's goal. 
* Javafx will be implemented in this program. 

### User

The user is able to enter their login details, create a cart with the items and quantities of items that they desire,
review those items and have them enetered to their account. 
	
### Admin

The manager, further refered to as admin is able to administer the program from a select user account. A new menu
appears when this account is invoked, allowing admin options such as adding new members or products, retreving billing
data, or reseting billing cycles. Every action required by the admin to run the store is encorporated into this menu. 
The admin is also the only user with the permissions to close the program for maintenance of the system. 
	
	
### Security

The programs security is heavily based on ease of use over security. The main security implementation is that of unknown
IDs. Users are unlikely to know the id of another and thus are unlikely to be able to log in. 
The admin account has it's own ID, completly different to the ids of the other users. 

Passwords have not been implemented for users in this system. However, they may become useful if security questions are raised in
regards to the incorrect use of the user systems. These would have to be hashed and added to the database. 

### Export

The program will come with a function which allows the user admin to save both the product and person databases to a specified folder in the CSV format.
