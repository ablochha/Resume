//
//  main.cpp
//  Bank System
//
//  Created by Andrew Bloch-hansen on 2015-10-12.
//  Copyright © 2015 Andrew Bloch-hansen. All rights reserved.
//

#include <iostream>
#include <fstream>
#include <cctype>
#include <iomanip>
#include <cstdlib>
#include <ctime>

using namespace std;

//This class describes a bank account
class BankAccount {
    
    int account_funds;  //the amount of money in the account
    bool account_open;  //whether the account is open or closed
    
public:
    
    void deposit(int);                  //deposit funds into the account
    void withdraw(int);                 //withdraw funds from the account
    int get_account_funds() const;      //view account funds
    void set_account_funds(int funds);  //set the account funds
    bool get_account_open();            //check if the account is open
    void set_account_open(bool open);   //set the account as open or closed
    
}; //end BankAccount

void BankAccount::deposit(int x) {
    
    account_funds+=x;
    
} //end deposit

void BankAccount::withdraw(int x) {
    
    account_funds-=x;
    
} //end withdraw

int BankAccount::get_account_funds() const {
    
    return account_funds;

} //end get_account_funds

void BankAccount::set_account_funds(int funds) {
    
    account_funds = funds;
    
} //end set_account_funds

bool BankAccount::get_account_open() {
    
    return account_open;
    
} //end get_account_open

void BankAccount::set_account_open(bool open) {
    
    account_open = open;
    
} //end set_account_open

//This class describes a bank user
class BankUser {
    
    char login_id[50];      //the login id for the user
    int role;               //the role of the user in the bank system
    BankAccount savings;    //a savings bank account
    BankAccount chequings;  //a chequings bank account
    
public:
    
    char* create_user();                        //form for the user to fill in
    void initialize_accounts();                 //initialize the accounts
    void show_details();                        //print out user details
    int get_role() const;                       //get user's role
    char* get_login_id();                       //get user's login id
    void deposit_into_savings(int funds);       //deposit into a users savings account
    void withdraw_from_savings(int funds);      //withdraw from a users savings account
    void deposit_into_chequings(int funds);     //deposit into a users chequings account
    void withdraw_from_chequings(int funds);    //withdraw from a users savings account
    int get_savings_funds();                    //view savings account funds
    int get_chequings_funds();                  //view chequings account funds
    bool get_savings_open();                    //check if savings account is open
    void set_savings_open(bool open);           //set the savings account to open or closed
    bool get_chequings_open();                  //check if the chequings account is open
    void set_chequings_open(bool open);         //set the chequings account to open or closed
    
}; //end BankUser

char* BankUser:: create_user() {
    
    cout<<"Enter your login id: ";
    cin.ignore();
    cin.getline(login_id,50);
    
    bool valid = false;
    
    while (valid == false) {
        
        system("clear");
        cout<<"Enter your role:\n1. Client\n2. Manager\n3. Maintenance\nSelect Your Option (1-3): ";
        cin>>role;
        
        if (role > 0 && role < 4)
            
            valid = true;
        
    }  //end while
    
    return login_id;
    
} //end add_new_user

void BankUser:: initialize_accounts() {
    
    savings.set_account_funds(0);
    savings.set_account_open(false);
    chequings.set_account_funds(0);
    chequings.set_account_open(false);
    
} //end initialize_accounts

void BankUser:: show_details() {
    
    cout<<"\nClient login id: \t\t"<<login_id<<endl;
    cout<<"Client role: \t\t\t"<<role<<endl;
    cout<<"Savings account balance : \t"<<savings.get_account_funds()<<endl;
    cout<<"Chequings account balance : \t"<<chequings.get_account_funds()<<endl;
    
} //end show_details

int BankUser::get_role() const {
    
    return role;
    
} //end get_role

char* BankUser::get_login_id() {
    
    return login_id;
    
} //end get_login_id

void BankUser::deposit_into_savings(int funds) {
    
    savings.deposit(funds);
    
} //end deposit_into_savings

void BankUser::withdraw_from_savings(int funds) {
    
    savings.withdraw(funds);
    
} //end withdraw_from_savings

void BankUser::deposit_into_chequings(int funds) {
    
    chequings.deposit(funds);
    
} //end deposit_into_chequings

void BankUser::withdraw_from_chequings(int funds) {
    
    chequings.withdraw(funds);
    
} //end withdraw_from_chequings

int BankUser::get_savings_funds() {
    
    return savings.get_account_funds();
    
} //end get_savings_funds

int BankUser::get_chequings_funds() {
    
    return chequings.get_account_funds();
    
} //end get_chequings_funds

bool BankUser::get_savings_open() {
    
    return savings.get_account_open();
    
} //end get_savings_open

void BankUser::set_savings_open(bool open) {
    
    savings.set_account_open(open);
    
} //end set_savings_open

bool BankUser::get_chequings_open() {
    
    return chequings.get_account_open();
    
} //end get_chequings_open

void BankUser::set_chequings_open(bool open) {
    
    chequings.set_account_open(open);
    
} //end set_chequings_open

//Returns the role of a user
//@param login_id the user's login id
//@return the role of the user
int get_user_role(char login_id[50]) {
    
    BankUser user;
    ifstream inFile;
    inFile.open("data.dat", ios::binary);
    
    if (!inFile) {
        
        return 0;
        
    } //end if

    while (inFile.read(reinterpret_cast<char *> (&user), sizeof(BankUser))) {
        
        if (strcmp(user.get_login_id(), login_id) == 0) {
            
            inFile.close();
            return user.get_role();
            
        } //end if
        
    } //end while
    
    inFile.close();
    return 0;
    
} //end get_user_role

//Check if a login id already exists
//@param login_id the user's login id
//@return true if the user exists otherwise false
bool login_exists(char login_id[50]) {
    
    BankUser user;
    ifstream inFile;
    inFile.open("data.dat",ios::binary);
    
    if(!inFile) {
        
        return false;
        
    } //end if
    
    while (inFile.read(reinterpret_cast<char *> (&user), sizeof(BankUser))) {
        
        if(strcmp(user.get_login_id(), login_id) == 0) {
            
            inFile.close();
            return true;
            
        } //end if
        
    } //end while
    
    inFile.close();
    return false;
    
} //end login_exists

//Writes a new user to file
void write_new_user() {
    
    BankUser user;
    char *login;
    
    login = user.create_user();
    
    if (!login_exists(login)) {
        
        user.initialize_accounts();
        ofstream outFile;
        outFile.open("data.dat", ios::binary|ios::app);
        outFile.write(reinterpret_cast<char *> (&user), sizeof(BankUser));
        outFile.close();
        cout<<"\nNew user created";
        
    } //end if
    
    else {
        
        cout<<"\nThis user already exists!";
        
    } //end else
    
    cin.ignore();
    cin.get();
    
} //end write_new_user

//Writes updates to a users savings account
//@param login_id the user's logid id
//@param option the specific update to the account
void manage_savings_account(char* login_id, int option) {
    
    BankUser user;
    fstream File;
    int funds;
    bool found = false;
    
    File.open("data.dat", ios::binary|ios::in|ios::out);
    
    if (!File) {
        
        cout<<"File could not be open !! Press any Key...";
        return;
        
    } //end if
    
    while(!File.eof() && found==false) {
        
        File.read(reinterpret_cast<char *> (&user), sizeof(BankUser));
        
        if(strcmp(user.get_login_id(), login_id) == 0) {
            
            if (option == 1 && user.get_savings_open() == false) {
                
                cout<<"A manager has opened a savings account for you.";
                user.set_savings_open(true);
                
            } //end if
            
            else if (option == 1 && user.get_savings_open() == true) {
                
                cout<<"You already have a savings account.";
                
            } //end else if
            
            if (option == 2 && user.get_savings_open() == true) {
                
                if (user.get_savings_funds() == 0) {
                    
                    cout<<"A manager has closed your savings account for you.";
                    user.set_savings_open(false);
                    
                } //end if
                
                else {
                    
                    cout<<"Only accounts with 0 funds can be closed.";
                    
                } //end else
                
            } //end if
            
            else if (option == 2 && user.get_savings_open() == false) {
                
                cout<<"You don't have an open savings account.";
                
            } //end else if
            
            if (option == 3 & user.get_savings_open() == true) {
                
                cout<<"Enter amount to deposit: ";
                cin.ignore();
                cin>>funds;
                user.deposit_into_savings(funds);
                cout<<"Current savings account balance: "<<user.get_savings_funds()<<endl;
                
            } //end if
            
            else if (option == 3 && user.get_savings_open() == false) {
                
                cout<<"You don't have an open savings account.";
                
            } //end else if
            
            if (option == 4 & user.get_savings_open() == true) {
                
                cout<<"Enter amount to withdraw: ";
                cin.ignore();
                cin>>funds;
                
                if ((user.get_savings_funds() - funds) < 0) {
                    
                    cout<<"Insufficient funds in account.";
                    
                } //end if
                
                else {
                    
                    user.withdraw_from_savings(funds);
                    cout<<"Current savings account balance: "<<user.get_savings_funds()<<endl;
                    
                } //end else
                
            } //end if
            
            else if (option == 4 && user.get_savings_open() == false) {
                
                cout<<"You don't have an open savings account.";
                
            } //end else if
            
            if (option == 5 & user.get_savings_open() == true) {
                
                if (user.get_chequings_open() == true) {
                    
                    cout<<"Enter amount to transfer: ";
                    cin.ignore();
                    cin>>funds;
                    
                    if (user.get_savings_funds() - funds < 0) {
                        
                        cout<<"Insufficient funds in account.";
                        
                    } //end if
                    
                    else {
                        
                        user.withdraw_from_savings(funds);
                        user.deposit_into_chequings(funds);
                        cout<<"Current savings account balance: "<<user.get_savings_funds()<<endl;
                        cout<<"Current chequings account balance: "<<user.get_chequings_funds()<<endl;
                        
                    } //end else
                    
                } //end if
                
                else {
                    
                    cout<<"You don't have an open chequings account.";
                    
                } //end else
                
            } //end if
            
            else if (option == 5 && user.get_savings_open() == false) {
                
                cout<<"You don't have an open savings account.";
                
            } //end else if
            
            if (option == 6 && user.get_savings_open() == true) {
                
                cout<<"Current savings account balance: "<<user.get_savings_funds()<<endl;
                
            } //end if
            
            else if (option == 6 && user.get_savings_open() == false) {
                
                cout<<"You don't have an open savings account.";
                
            } //end else if
            
            int pos=(-1)*static_cast<int>(sizeof(user));
            File.seekp(pos,ios::cur);
            File.write(reinterpret_cast<char *> (&user), sizeof(BankUser));
            found=true;
            
        } //end if
        
    } //end while
    
    File.close();
    cin.get();
    
} //end manage_savings_account

//Writes updates to a users chequings account
//@param login_id the user's logid id
//@param option the specific update to the account
void manage_chequings_account(char* login_id, int option) {
    
    BankUser user;
    fstream File;
    bool found = false;
    int funds;
    File.open("data.dat", ios::binary|ios::in|ios::out);
    
    if (!File) {
        
        cout<<"File could not be open !! Press any Key...";
        return;
        
    } //end if
    
    while(!File.eof() && found==false) {
        
        File.read(reinterpret_cast<char *> (&user), sizeof(BankUser));
        
        if(strcmp(user.get_login_id(), login_id) == 0) {
            
            if (option == 1 && user.get_chequings_open() == false) {
                
                cout<<"A manager has opened a chequings account for you.";
                user.set_chequings_open(true);
                
            } //end if
            
            else if (option == 1 && user.get_chequings_open() == true) {
                
                cout<<"You already have a chequings account.";
                
            } //end else if
            
            if (option == 2 && user.get_chequings_open() == true) {
                
                if (user.get_chequings_funds() == 0) {
                    
                    cout<<"A manager has closed your chequings account for you.";
                    user.set_chequings_open(false);
                    
                } //end if
                
                else {
                    
                    cout<<"Only accounts with 0 funds can be closed.";
                    
                } //end else
                
            } //end if
            
            else if (option == 2 && user.get_chequings_open() == false) {
                
                cout<<"You don't have an open chequings account.";
                
            } //end else if
            
            if (option == 3 & user.get_chequings_open() == true) {
            
            cout<<"Enter amount to deposit: ";
            cin.ignore();
            cin>>funds;
            user.deposit_into_chequings(funds);
            cout<<"Current chequings account balance: "<<user.get_chequings_funds()<<endl;
            
            } //end if
        
            else if (option == 3 && user.get_chequings_open() == false) {
            
                cout<<"You don't have an open chequings account.";
            
            } //end else if
        
            if (option == 4 & user.get_chequings_open() == true) {
            
                cout<<"Enter amount to withdraw: ";
                cin.ignore();
                cin>>funds;
            
                if ((user.get_chequings_funds() - funds) < 0) {
                
                    cout<<"Insufficient funds in account.";
                
                } //end if
            
                else if ((user.get_chequings_funds() - funds) < 1000) {
                
                    cout<<"Warning - you will drop below $1000 and be charged an additional $2. Proceed? (y/n): ";
                    char ch;
                    cin.ignore();
                    cin>>ch;
                
                    if (ch == 'y' || ch == 'Y') {
                    
                        user.withdraw_from_chequings(funds+2);
                        cout<<"Current chequings account balance: "<<user.get_chequings_funds()<<endl;
                    
                    } //end if
                
                } //end else if
            
                else {
                
                    user.withdraw_from_chequings(funds);
                    cout<<"Current chequings account balance: "<<user.get_chequings_funds()<<endl;
                
                } //end else
            
            } //end if
        
            else if (option == 4 && user.get_chequings_open() == false) {
            
                cout<<"You don't have an open chequings account.";
            
            } //end else if
        
            if (option == 5 & user.get_chequings_open() == true) {
            
                if (user.get_savings_open() == true) {
                
                    cout<<"Enter amount to transfer: ";
                    cin.ignore();
                    cin>>funds;
                
                    if (user.get_chequings_funds() - funds < 0) {
                    
                        cout<<"Insufficient funds in account.";
                    
                    } //end if
                
                    else if ((user.get_chequings_funds() - funds) < 1000) {
                    
                        cout<<"Warning - you will drop below $1000 and be charged an additional $2. Proceed? (y/n): ";
                        char ch;
                        cin.ignore();
                        cin>>ch;
                    
                        if (ch == 'y' || ch == 'Y') {
                        
                            user.withdraw_from_chequings(funds+2);
                            user.deposit_into_savings(funds);
                            cout<<"Current chequings account balance: "<<user.get_chequings_funds()<<endl;
                            cout<<"Current savings account balance: "<<user.get_savings_funds()<<endl;
                        
                        } //end if
                    
                    } //end else if
                
                    else {
                    
                        user.withdraw_from_chequings(funds);
                        user.deposit_into_savings(funds);
                        cout<<"Current chequings account balance: "<<user.get_chequings_funds()<<endl;
                        cout<<"Current savings account balance: "<<user.get_savings_funds()<<endl;
                    
                    } //end else
                
                } //end if
            
                else {
                
                    cout<<"You don't have an open savings account.";
                
                } //end else
            
            } //end if
        
            else if (option == 5 && user.get_chequings_open() == false) {
            
                cout<<"You don't have an open chequings account.";
            
            } //end else if
        
            if (option == 6 && user.get_chequings_open() == true) {
            
                cout<<"Total funds in chequings account: "<<user.get_chequings_funds()<<endl;
            
            } //end if
        
            else if (option == 6 && user.get_chequings_open() == false) {
            
                cout<<"You don't have an open chequings account.";
            
            } //end else if
            
            int pos=(-1)*static_cast<int>(sizeof(user));
            File.seekp(pos,ios::cur);
            File.write(reinterpret_cast<char *> (&user), sizeof(BankUser));
            found=true;
            
        } //end if
        
    } //end while
    
    File.close();
    cin.ignore();
    cin.get();
    
} //end manage_chequings_account

//Views the details of a specific client
//@param client_id the clients login id
void view_client(char* client_id) {
    
    BankUser user;
    ifstream inFile;
    inFile.open("data.dat",ios::binary);
    
    if(!inFile) {
        
        cout<<"File could not be open !! Press any Key...";
        return;
        
    } //end if
    
    while (inFile.read(reinterpret_cast<char *> (&user), sizeof(BankUser))) {
        
        if(strcmp(user.get_login_id(), client_id) == 0) {
            
            user.show_details();
            inFile.close();
            //cin.ignore();
            cin.get();
            return;
            
        } //end if
        
    } //end while
    
    inFile.close();
    cout<<"\nCould not find user";
    cin.ignore();
    cin.get();
    
} //end view_client

//Views the details of all the existing clients
void view_all_clients() {
    
    BankUser user;
    ifstream inFile;
    inFile.open("data.dat",ios::binary);
    
    if(!inFile) {
        
        cout<<"File could not be open !! Press any Key...";
        return;
        
    } //end if
    
    while (inFile.read(reinterpret_cast<char *> (&user), sizeof(BankUser))) {
        
        user.show_details();
        
    } //end while
    
    inFile.close();
    cin.ignore();
    cin.get();
    
} //end view_all_clients

//Views the data from all clients of the bank
void view_bank_data() {
    
    BankUser user;
    int role;
    int num_users = 0;
    int num_clients = 0;
    int num_managers = 0;
    int num_maintenance = 0;
    int num_open_savings_accounts = 0;
    int num_open_chequings_accounts = 0;
    int bank_savings_funds = 0;
    int bank_chequings_funds = 0;
    ifstream inFile;
    inFile.open("data.dat",ios::binary);
    
    if(!inFile) {
        
        cout<<"File could not be open !! Press any Key...";
        return;
        
    } //end if
    
    while (inFile.read(reinterpret_cast<char *> (&user), sizeof(BankUser))) {
        
        num_users++;
        role = user.get_role();
        
        if (role == 1)
            
            num_clients++;
        
        else if (role == 2)
            
            num_managers++;
        
        else if (role == 3)
            
            num_maintenance++;
        
        if (user.get_savings_open() == true)
            
            num_open_savings_accounts++;
        
        if (user.get_chequings_open() == true)
            
            num_open_chequings_accounts++;
        
        bank_savings_funds+= user.get_savings_funds();
        bank_chequings_funds+= user.get_chequings_funds();
        
    } //end while
    
    cout<<"The number of bank users: \t\t\t"<<num_users<<endl;
    cout<<"Clients: \t\t\t\t\t"<<num_clients<<endl;
    cout<<"Managers: \t\t\t\t\t"<<num_managers<<endl;
    cout<<"Maintenance: \t\t\t\t\t"<<num_maintenance<<endl;
    cout<<"The number of open savings accounts: \t\t"<<num_open_savings_accounts<<endl;
    cout<<"Total savings accounts funds: \t\t\t"<<bank_savings_funds<<endl;
    cout<<"The number of open chequings accounts: \t\t"<<num_open_chequings_accounts<<endl;
    cout<<"Total chequings accounts funds: \t\t"<<bank_chequings_funds<<endl;
    cout<<"Total bank funds: \t\t\t\t"<<bank_savings_funds+bank_chequings_funds<<endl;

    inFile.close();
    cin.ignore();
    cin.get();
    
} //end view_bank_data

//Deletes a client from the bank system
//@param client_id the client's login id
void delete_client(char* client_id) {
    
    BankUser user;
    ifstream inFile;
    ofstream outFile;
    inFile.open("data.dat",ios::binary);
    
    if(!inFile) {
        
        cout<<"File could not be open !! Press any Key...";
        return;
 
    } //end if
    
    outFile.open("temp.dat",ios::binary);
    inFile.seekg(0,ios::beg);
    
    while(inFile.read(reinterpret_cast<char *> (&user), sizeof(BankUser))) {
        
        if(strcmp(user.get_login_id(), client_id) != 0)
            
            outFile.write(reinterpret_cast<char *> (&user), sizeof(BankUser));
        
    } //end while
    
    inFile.close();
    outFile.close();
    remove("data.dat");
    rename("Temp.dat","data.dat");
    cout<<"\n"<<client_id<<" has been deleted";
    cin.ignore();
    cin.get();
    
} //end delete_client

//Menu for a client to manage their savings account
//@param login_id the clients login id
//@param execution_trace the maintenance person's trace
//@param maintenance_file the maintenance output file
void manage_savings(char* login_id, bool execution_trace, ofstream &maintenance_file) {
    
    char ch;
    bool menu = true;
    
    while (menu) {
        
        if (execution_trace == true)
            
            maintenance_file<<"Entering manage savings screen\n";
        
        system("clear");
        cout<<"Welcome "<<login_id<<endl;
        cout<<"\nSavings account";
        cout<<"\n1. Request a manager to open a savings account";
        cout<<"\n2. Request a manager to close your savings account";
        cout<<"\n3. Deposit";
        cout<<"\n4. Withdraw";
        cout<<"\n5. Transfer funds to your chequings account";
        cout<<"\n6. View balance";
        cout<<"\n7. Back";
        cout<<"\nSelect Your Option (1-7): ";
        cin>>ch;
        system("clear");
        
        switch(ch) {
                
            case '1':
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Entering open savings screen\n";
                
                manage_savings_account(login_id, 1);
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Exiting open savings screen\n";
                
                break;
                
            case '2':
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Entering close savings screen\n";
                
                manage_savings_account(login_id, 2);
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Exiting close savings screen\n";
                
                break;
                
            case '3':
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Entering deposit in savings screen\n";
                
                manage_savings_account(login_id, 3);
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Exiting deposit in savings screen\n";
                
                break;
                
            case '4':
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Entering withdraw from savings screen\n";
                
                manage_savings_account(login_id, 4);
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Exiting withdraw from savings screen\n";
                
                break;
                
            case '5':
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Entering transfer from savings screen\n";
                
                manage_savings_account(login_id, 5);
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Exiting transfer from savings screen\n";
                
                break;
                
            case '6':
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Entering view savings balance screen\n";
                
                manage_savings_account(login_id, 6);
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Exiting view savings balance screen\n";
                
                break;
                
            case '7':
                
                menu = false;
                break;
                
            default:
                
                cout<<"\a";
                
        } //end switch
        
        cin.get();
        
    } //end while
    
    if (execution_trace == true)
        
        maintenance_file<<"Exiting manage savings screen\n";
    
} //end manage_savings

//Menu for a client to manage their chequings account
//@param login_id the clients login id
//@param execution_trace the maintenance person's trace
//@param maintenance_file the maintenance output file
void manage_chequings(char* login_id, bool execution_trace, ofstream &maintenance_file) {
    
    char ch;
    bool menu = true;
    
    while (menu) {
        
        if (execution_trace == true)
            
            maintenance_file<<"Entering manage chequings screen\n";
        
        system("clear");
        cout<<"Welcome "<<login_id<<endl;
        cout<<"\nChequings account";
        cout<<"\n1. Request a manager to open a chequings account";
        cout<<"\n2. Request a manager to close your chequings account";
        cout<<"\n3. Deposit";
        cout<<"\n4. Withdraw";
        cout<<"\n5. Transfer funds to your savings account";
        cout<<"\n6. View balance";
        cout<<"\n7. Back";
        cout<<"\nSelect Your Option (1-7): ";
        cin>>ch;
        system("clear");
        
        switch(ch) {
                
            case '1':
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Entering open chequings screen\n";
                
                manage_chequings_account(login_id, 1);
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Exiting open chequings screen\n";
                
                break;
                
            case '2':
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Entering close chequings screen\n";
                
                manage_chequings_account(login_id, 2);
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Exiting close chequings screen\n";
                
                break;
                
            case '3':
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Entering deposit in chequings screen\n";
                
                manage_chequings_account(login_id, 3);
                
                if (execution_trace == true)
                    
                    maintenance_file<<"EExiting deposit in chequings screen\n";
                
                break;
                
            case '4':
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Entering withdraw from chequings screen\n";
                
                manage_chequings_account(login_id, 4);
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Exiting withdraw from chequings screen\n";
                
                break;
                
            case '5':
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Entering transfer from chequings screen\n";
                
                manage_chequings_account(login_id, 5);
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Exiting transfer from chequings screen\n";
                
                break;
                
            case '6':
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Entering view chequings balance screen\n";
                
                manage_chequings_account(login_id, 6);
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Exiting view chequings balance screen\n";
                
                break;
                
            case '7':
                
                menu = false;
                break;
                
            default:
                
                cout<<"\a";
                
        } //end switch
        
    } //end while
    
    if (execution_trace == true)
        
        maintenance_file<<"Exiting manage chequings screen\n";
    
} //end manage_savings

//Menu for a client to access their data
//@param login_id the clients login id
//@param execution_trace the maintenance person's trace
//@param maintenance_file the maintenance output file
void client(char* login_id, bool execution_trace, ofstream &maintenance_file) {

    char ch;
    bool menu = true;
    
    while (menu) {
        
        if (execution_trace == true)
            
            maintenance_file<<"Entering client screen\n";
        
        system("clear");
        cout<<"Welcome "<<login_id<<endl;
        cout<<"Client\n";
        cout<<"\n1. Manage savings account";
        cout<<"\n2. Manage chequings account";
        cout<<"\n3. Back";
        cout<<"\nSelect Your Option (1-3): ";
        cin>>ch;
        system("clear");
        
        switch(ch) {
                
            case '1':
                
                manage_savings(login_id, execution_trace, maintenance_file);
                break;
                
            case '2':
                
                manage_chequings(login_id, execution_trace, maintenance_file);
                break;
                
            case '3':
                
                menu = false;
                break;
                
            default:
                
                cout<<"\a";
                
        } //end switch
      
    } //end while
    
    if (execution_trace == true)
        
        maintenance_file<<"Exiting client screen\n";
    
} //end client

//Menu for a manager to access their data
//@param login_id the clients login id
//@param execution_trace the maintenance person's trace
//@param maintenance_file the maintenance output file
void manager(char* login_id, bool execution_trace, ofstream &maintenance_file) {
    
    char ch;
    char client_id[50];
    bool menu = true;
    
    while (menu) {
        
        if (execution_trace == true)
            
            maintenance_file<<"Entering manager screen\n";
        
        system("clear");
        cout<<"Welcome "<<login_id<<endl;
        cout<<"Manager\n";
        cout<<"\n1. View specific client details";
        cout<<"\n2. View all customer details";
        cout<<"\n3. Delete a specific client";
        cout<<"\n4. View bank data";
        cout<<"\n5. Log in as client";
        cout<<"\n6. Back";
        cout<<"\nSelect Your Option (1-6): ";
        cin>>ch;
        system("clear");
        
        switch(ch) {
                
            case '1':
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Entering view specific client screen\n";
                
                cout<<"Enter the client's login id: ";
                cin.ignore();
                cin.getline(client_id,50);
                view_client(client_id);
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Exiting view specific client screen\n";
                
                break;
                
            case '2':
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Entering view all clients screen\n";
                
                view_all_clients();
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Exiting view all clients screen\n";
                
                break;
                
            case '3':
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Entering delete a specific client screen\n";
                
                cout<<"Enter the client's login id: ";
                cin.ignore();
                cin.getline(client_id, 50);
                
                cout<<"Warning - are you sure you want to delete? (y/n): ";
                char ch;
                cin>>ch;
                
                if (ch == 'y' || ch == 'Y') {
                    
                    delete_client(client_id);
                    
                } //end if
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Exiting delete a specific client screen\n";
                
                break;
                
            case '4':
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Entering view bank data screen\n";
                
                view_bank_data();
                
                if (execution_trace == true)
                    
                    maintenance_file<<"Exiting view bank data screen\n";
                
                break;
                
            case '5':
                
                client(login_id, execution_trace, maintenance_file);
                break;
                
            case '6':
                
                menu = false;
                break;
                
            default:
                
                cout<<"\a";
                
        } //end switch
        
    } //end while
    
    if (execution_trace == true)
        
        maintenance_file<<"Exiting manager screen\n";
    
} //end manager

//Menu for a maintenance person
//@param login_id the clients login id
//@param execution_trace the maintenance person's trace
void maintenance(char* login_id, bool *execution_trace) {
    
    char ch;
    bool menu = true;
    
    while (menu) {
        
        system("clear");
        cout<<"Welcome "<<login_id<<endl;
        cout<<"Maintenance\n";
        cout<<"\n1. Turn on execution trace";
        cout<<"\n2. Turn off execution trace";
        cout<<"\n3. Back";
        cout<<"\nSelect Your Option (1-3): ";
        cin>>ch;
        system("clear");
        
        switch(ch) {
                
            case '1':
                
                cout<<"Execution trace on.";
                (*execution_trace) = true;
                cin.ignore();
                cin.get();
                break;
                
            case '2':
                
                cout<<"Execution trace off.";
                (*execution_trace) = false;
                cin.ignore();
                cin.get();
                break;
                
            case '3':
                
                menu = false;
                break;
                
            default:
                
                cout<<"\a";
                
        } //end switch
        
    } //end while
    
} //end maintenance

//Displays the introduction menu
void intro() {
    
    char ch;
    char login_id[50];
    int role;
    bool menu = true;
    bool execution_trace = false;
    
    ofstream maintenance_file;
    maintenance_file.open("maintenance.txt", ios::app);
    
    while (menu) {
        
        system("clear");
        cout<<"Welcome to the bank\n";
        cout<<"\n1. Login";
        cout<<"\n2. Add new user";
        cout<<"\n3. Exit";
        cout<<"\nSelect Your Option (1-3): ";
        cin>>ch;
        system("clear");
        
        switch(ch) {
                
            case '1':
                
                cout<<"Welcome to the bank\n";
                cout<<"\nPlease enter your login id: ";
                cin.ignore();
                cin.getline(login_id,50);
                
                if (login_exists(login_id)) {
                    
                    if (execution_trace == true) {
    
                        time_t t = time(0);     //get time now
                        struct tm *now = localtime(&t);
                        maintenance_file<<(now->tm_year + 1900)<<'-'<<(now->tm_mon + 1)<<'-'<<now->tm_mday<<endl;
                        maintenance_file<<"User: "<<login_id<<endl;
                        
                    } //end if
                    
                    role = get_user_role(login_id);
                    
                    if (role == 1)
                        
                        client(login_id, execution_trace, maintenance_file);
                    
                    else if (role == 2)
                        
                        manager(login_id, execution_trace, maintenance_file);
                    
                    else if (role == 3)
                        
                        maintenance(login_id, &execution_trace);
                    
                    else
                        
                        cout<<"Invalid role";
                    
                } //end if

                else {
                    
                    cout<<"That user does not exist\n";
                    cin.get();
                    
                } //end else
                
                break;
                
            case '2':
                
                write_new_user();
                break;
                
            case '3':
                
                cout<<"Thanks for using the bank\n\n";
                menu = false;
                break;
                
            default :
                
                cout<<"\a";
                
        } //end switch
      
    } //end while
    
    if (maintenance_file)
        
        maintenance_file.close();
    
} //end intro

int main(int argc, const char *argv[]) {
    // insert code here...

    intro();
    return 0;
    
} //end main