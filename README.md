# How to setup Local Development

## Django

1. Run your server at 0.0.0.0 on port 8000

   `python manage.py runserver 0.0.0.0:8000`



2. Get your local computer IP

   - Open a terminal
   - Run `ipconfig` might be different for mac `man ipconfig`?
   - Look for an `IPV4 Address` not `Default Gateway`
   - Use this address to access Django and copy this somewhere. We will need this for the Android section.

# Android

1. Head to `frontend/` and find `local.properties`

2. Add a line to the file, and place your IP you found earlier in the appropriate field

   `API_URL="http://<YOUR_IP>:8000/"`

   - quotes are important here
   - the last slash is important

3. Your file should look like this now

   ```
   sdk.dir=C\:\\Users\\tangj\\AppData\\Local\\Android\\Sdk
   API_URL="http://<YOUR_IP>:8000/"
   ```



# Done!

Now you should be able to connect to the Django API with Android.



# Directory Structure

## High Level

At the root directory there are two folders `backend` and `frontend`.

- frontend contains the source code for the Android app that act as the user interface for communicating with the backend
- backend contains the source code for the Django server that maintains the API, models, and database operations.

## Backend directory

Here we follow the standard Django folder structure that gets generated when a project gets initialized. Each component in the app gets their own folder. i.e. clients, visits, alerts ... as an application or sub application. Within each app we have definitions of models, serializers to serialize the model, and API endpoints to expose the model to operations via HTTP methods.

- models.py - where models get defined.
- views.py - where the API logic of endpoints get defined.
- serializer.py - the serializer that serializes a model.

`cbrsite` is the root app for the Django project where new apps are registered into `cbrsite/settings.py`. It also contains are `urls.py` that controls routing for the entire Django project.

`images` contains the media that gets uploaded to the server. Examples includes client and user photos.

One special addition is a `scripts` folder that contains scripts that help us with development. For example, one script generates and populates clients with random data into our database.

For dependencies, `requirements.txt` contains the needed dependencies to run the Django project.

## Frontend directory

Here we follow the standard Android folder structure that gets generated by Android studios.

For logic and the packages, we have the following organization:

- `service` package - contains all the services, and models that defines and manages requests to be sent to the backend. These services are responsible for deserializing JSON to Java objects from requests responses so we can manipulate them in the Java environment.
- `ui` package - contains our activities, fragments, and any components or logic that have to deal with the user interface. This includes our login page, client listing page, etc.
- `utils` package - contains helper and utility classes to be used through the entire Android app.

For layouts, that is `frontend\app\src\main\res\layout`, contains our UI elements. They are placed in flat folder structure and are referenced as needed.

`assets` contains our static front end assets.

`app/src/build.gradle` contains the dependencies for the application.

# Project Build Requirements

## Android Studio
1. Download the latest version of Android studio from [this link](https://developer.android.com/studio), available for Windows, Mac, Linux and Chrome OS.

2. Follow the download and installation instruction until you open Android Studio

 ![Android studio](/readme-images/build-setup01.PNG)

### Android Emulator
Android Emulator can be use on [Windows](#windows), [Mac](#mac) and [Linux](#linux)

#### Windows
1. Go to Control Panel and search `feature` on the search box.

2. Click on `Turn windows feature on/off` and make sure Hyper-V/Windows Hypervisor Platform or any other virtual machine features are unchecked

 ![unchecked](/readme-images/build-setup02.PNG)

 you will need to restart your computer.

3. If you are using AMD processor go to [this step](#amd-processor), otherwise if you are using Intel processor continue on.

##### Intel processor
1. Open Android studio and go to Configure>SDK manager>SDK Update sites

 ![SDK Update sites](/readme-images/build-setup03.PNG)

2. Select and install Intel HAXM

3. Follow the instruction, after installation you can check if it is installed correctly by typing `sc query intelhaxm` on the Window command prompt

 ![command prompt](/readme-images/build-setup04.PNG)

if you can see `STATE 4 RUNNING` it mean the installation run correctly

4. Go to [AVD Setup](#android-virtual-device-setup)

##### AMD processor
1. Open Android studio and go to Configure>SDK manager>SDK Tool

 ![SDK Tool](/readme-images/build-setup05.PNG)

2. Select and install Android Emulator Hypervisor Driver for AMD Processors.

3 Follow the instruction, after installation you can check if it is installed correctly by typing `sc query gvm` on the Window command prompt, if you can see `STATE 4 RUNNING` it means the installation run correctly.

4. Go to [AVD Setup](#android-virtual-device-setup)

#### Mac
1. On MacOS X v10.10 Yosemite or higher, Android emulator use the built-in Hypervisor.Framework, if your MacOS version is lower you can go to Android studio Configure>SDK Manager>SDK Update sites and install Intel HAXM

2. Go to [AVD Setup](#android-virtual-device-setup)
#### Linux
1. For both Intel and AMD processor you need to install KVM, first check whether kvm is already installed on your system by using cpu-checker and kvm-ok commmand, run these commands in order

 cpu-checker
 ```
 sudo apt-get install cpu-checker
 egrep -c '(vmx|svm)' /proc/cpuinfo
 ```

 kvm-ok
 ```
 kvm-ok
 ```

 Final result should show `KVM acceleration can be used`

2. If KVM is not installed on your system, run the following command
 ```
 sudo apt-get install qemu-kvm libvirt-bin ubuntu-vm-builder bridge-utils ia32-libs-multiarch
 ```

3. Go to [AVD Setup](#android-virtual-device-setup)
#### Android Virtual Device Setup
1. Go to Android studio>Configure>AVD Manager

 ![AVD Manager](/readme-images/build-setup06.PNG)

2. Create the virtual device you want to test the application on, this application is designed for Phone type android devices. API level can be as high as you want - for reference, according to April 2020 survey, over 30% of android devices use Pie API level 28 (also called Pie 9)

3. Finish the creation process and wait for download.

## Django Virtual environment
1. Download and install Python 3.9 from [this link](https://www.python.org/downloads/release/python-390/)

2. Open the project backend folder [(backend directory explanation)](#backend-directory)

3. Open a terminal or Windows Command prompt in this directory and run the following commands in order
 ```
 python -m venv venv
 For Linux: source venv/bin/activate
 For Windows: venv\Scripts\activate

 pip install django
 pip install djangorestframework
 ```

4. Follow the instruction from [How to setup local development](#how-to-setup-local-development)



## Peter's Contribution

### Dashboard

The dashboard was created with the vision of something like a newsfeed, where a user can view important information such as the alerts and statistics drawn from all sections of the app.

**User story:** As a user, I want to be able to see alerts from the dashboard, so I can priorituze clients based on urgency

**User story:** As an admin, I want to be able to view various CBR statistics, so I can monitor the CBR team's progress.
* The dashboard is split into sections: alerts, high priority clients, visits, and referrals (for the future).  The Alerts section shows the most recent alert sent by an admin. The High Priority Clients Section shows the top 5 highest risk clients, along with two buttons to view all clients and add clients.
* The Visits section show statistics about visits: total visits made, locations visited, total clients visited.

**Unfinished parts:**
1. The referral section of the dashboard needs to be filled out.
2. Further statistics on clients likely to be added (Ex: total number of clients, "hotspot" locations that have many clients, ...)

**Questions for the customer:**
1. What is your impression of the dashboard? Are there any further statistics you would like the dashboard to show?
2. Are there any sections you would like to see added to the dashboard?
3. In slide 16, it seems that "dashboard with alert system" is different from the "home" page. Is there a difference between dashboard and home page, and would you like a "home" page separate from the dashboard?

### Clients List
The clients list allows the user to quickly view basic information about all clients. If necessary, the user may select a user in order to view more specific information.

**User story:** As a user, I want to be able to search for one or some of my clients, so I can monitor and record client related activity
* The client list currently shows the profile picture, name, location, and risk rating for each client. The client and then be selected so the user may view additional details and create a visit.

**Unfinished:**

1. The Client list is currently not searchable.
2. Determine how the list should be sorted.

**Questions for the customer:**

1. Would you like any additional information to be shown for each client on the list?
2. Currently, the list is sorted by the date the client was added. How would you like the list to be sorted? (By last name, by risk level, by first name, ...)
3. Are there any other features you would like added to the client list?

### Creation of New Visits

The creation of a new visit can currently be accessed through a button found in the details of a client. The survey is compiled into one scrollable activity. Once submitted, the data is uploaded to the backend.

**User Story:** As a user, I want to be able to record and edit client/visit data, so I can better manage my duties.

The New Visit Survey follows the logic listed:
1. If CBR is not selected as a purpose, then all provision sections are visible.
2. If CBR is selected as a purpose, the provisions are hidden unless the user selects a provision to be visible using the chips.
3. Descriptions of provisions and goal conclusion descriptions are hidden unless the user selects the provision chip or selects the "goal met" radio button.

The flow of the survey is an adaptation of our Piazza post, question #47.

**Unfinished parts:**

1. Implementation of required fields is missing. User must not be able to submit a visit survey unless required fields are filled out.

**Questions for the customer:**

1. Is the flow of the visit survey correct? It seems that if CBR is **NOT** selected as a purpose, then the provisions were to still be shown. They are only optionally hidden when CBR **IS** selected as a purpose.

2. In terms of user interface, are there any further suggestions for how the survey should look? Would you like the font sizes to be larger? Would you like more visuals such as colours or images?

### Navigation Drawer
The navigation bar acts as an index for the app, where the user can quickly jump between different sections such as client list, visit list, dashboard, and client creation.

**Unfinished parts:**
1. A logout/close button within the navigation drawer.

### User Interface Consistency

This was an additional task undertook to ensure that the interface has a consistency (such as the same font, same background colour for buttons).

**Questions for the customer:**

1. Overall, what is your impression of how the app looks? Are there any particular sections which especially do not resonate with you?
2. Regarding the colour pallette, we have chosen purple to be the app's main colour. Do you have any other colour scheme in mind?
3. We have tried to include a minimal amount of colours, leaving most of the app to be white. In terms of visuals, would you like the user interface to be more colourful? Or to have more images?
4. Are there any comparable sections in apps that you enivision this app could look like (for example, the profile section of the Facebook app, the lists of songs in the Spotify app)? This could help us to modify the interface to look more as you imagine it to look.


## Andrew's Contribution

### Creation of New Users

**User story:**  (#1)
As an admin, I want to be able to edit all user (CBR worker, clinician) data, so I can perform management duties if input was incorrect or priorities changed

* User creation(new user registration),including making the user creation page in android and uploading the new user info to the backend.

**Unfinished parts:**

1. Implementing the different levels of permission for each type of user (admin being able to delete/create new users and send alerts while normal users should not)

2. Other requirements related to management that comes up during feedback

**Questions for the customer:**

1. Is two levels of user permission enough? (CBR workers, admin) Or do we need a seperate user type for another class of people, e.g. Clinicians

2. If so, what should the difference be in what data and functions they can access?

3. Would deleting a user be of priority? Or would disabling a user be fine? (other fields such as visits/clients have dependencies on user information and would need to be deleted as well)


### Alerts Creation, List and Details

**User story:**  (#7)
As a user, I want to be able to see alerts from the dashboard, so I can prioritize clients/activities based on urgency

* The alerts Object was created both in the backend and frontend, so the admin/user can broadcast alert messages to the server where other users will retrieve from.
* The alerts include a title, body and date.
* The newest alerts(currently 1) show up in the the dashboard

**Unfinished parts:**

1. Adding the ability to specify which users the alert messages are desginated to.

2. Adding an urgency tag/flag and the ability to mark each message as read.

**Questions for the customer:**

1. Are there any additional function apart from "urgency tag/flag and mark each message as read" the customer needs?

2. Any UI tinkering needed on the alert list/ alert details pages?


## Kevin's Contribution

### Client details activity

*User story:** (#5)
As a user, I want to be able to view all my clients/visits, so I can monitor and record client related activity

* Client details, listing all relevant information about the client

*User story:** (4)
As a user, I want to be able to record and edit client/visit data, so I can better manage my duties

* Button on client details page which routes to edit client form
* Button on visits list page which routes to create visit page

**Unfinished parts: **

1. Implement the form where a user will be able to update client details by submitting new information to the backend
2. Implement ability to edit visit information by swiping on visit items in visit list

**Questions for the customer:**

1. On the visits page, do you want to be able to edit visits by swiping the visit items to see a menu?  Or to
 have a button on the visit details page which is accessible by tapping the visit items in the visits list

2. On the clients details page, how is the layout of the buttons at the bottom? (New visit, see visits, edit and back).
Are these too many buttons?

3. Would you prefer to have a dedicated back button integrated into each accessible part of the user interface (such as the back button on the client details page)
or are you okay with just using the back button which is supplied with Android OS?

4. Is the button size too large on the visits page?  How do you feel about the overall UI of the visits page.  Should the create button be relocated elsewhere?

5. How do you feel about the attributes which are present on the client details page?  (such as health, social, education) are there any more which you would like to add?


## Kun Hyung Park(Arthur)'s Contribution

### Creation of New Clients

**User story:**  (#4)
As an admin, I want to be able to create new clients' data, so I can track their progress and keep data as reference

* Client creation ,including making the client creation page in android and uploading the new client info to the backend.


**Unfinished parts:**

1. Implementing the camera when taking photo of the client + caretaker if they are present.

2. Other survey requirements such as health risk, social risk, and education risk. Currently they are set to 0.

**Questions for the customer:**

1. Is the flow of the create client survey page okay? Or do you want us to follow the exact order of the powerpoint slides?

2. There are two parts to the survey, one part where the client can fill out information themselves, and another part where they fill it out together with a CBR worker. Currently only the first part is implemented. For the second part, would a separate survey page be better? Or combine them into one?

## Sean's Contribution

### Visit management/details
After a user creats a visits for clients, there needs to be a convenient way for the user to view these visits. If accessing the page from the navigation bar, all visits will be shown. Users can also see a list of visits for specific clients, and then see more details for those visits.

**User Story** (#5)
As a user, I want to be able to view all my clients/visits, So I can monitor and record client related activity

* The items in the visit list include the client's name and number
* Visits can be viewed either by client or in one big list
* The per-client visits page uses nearly all the same logic as the full visits page, but it is passed the clientID and filters by it.
* The visits details page for each visit also shows additional details

**Unfinished parts:**

1. The visit list should show the date of the visit, and perhaps sort by that date

2. Some information on the visits details page is currently hardcoded for display purposes. This should be fixed.

**Questions for the customer:**

1. Would it ever be useful to view all visits in one place, or would it only be useful to view visits on a per-client basis?

### Login page
The login page allows users to log in to reach their personalized home screen

**User Story** (#1)
As an admin, I want to be able to edit all user (CBR worker, clinician) data, so I can perform management duties if input was incorrect or priorities changed

## William Tran's Contribution

### Creation of New Clients
**User story:** (#4)
As a user, I want to be able to record my client's data to a remote server so I can manage my duties.

*Client models and image root for upload and saving client data.

**Unfinished parts:**

1. Implement search function from Backend

**Questions for the customer:**

1. If given the keyword options to search clients from the list, what kind of options would you like to for search function? For example you can search by locations, name, type of disability.
It is possible to search for multiple keywords if all of the keywords are relevant to the client's data, but it is difficult to search for client's data using keyword from the visit data.


## Vincent's Contribution

### Initial creation of user creations for the backend

**User story:**  (#1)
As an admin, I want to be able to edit all user (CBR worker, clinician) data, so I can perform management duties if input was incorrect or priorities changed

* Created the backend info for the user such that we can create or modify users in the backend. This includes a username, password, email, first name, last name

**Unfinished parts:**

1. May need to include other information associated with the user.

**Questions for the customer:**

1. Are there additional fields that you want to add to users? such as age or location?

### Optimizing and refactoring of client creation

**User story:**  (#4)
As a user, I want to be able to record and edit client/visit data, so I can better manage my duties.

* Modified the create clients page such that it uses fragments rather than activities. This would give some performance gain.
