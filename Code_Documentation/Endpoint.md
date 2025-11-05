# Endpoints

### Total endpoint: 17

## List of Endpoints available

> Note: This all primary used by jsp(SSR) stricly not REST  


- #### /landing
    - Servlet: AuthenticationController
    - Main entry point for application
    - Show little about app and provide option for either login or signup
    and redirect respectively
    - Method: Get
    - No login required

- #### /login
    - Servlet: AuthenticationController
    - Login page/point of application
    - Currently login only available via combination of mail id and password
    - Method: Get, POST
    - No login required

- #### /signup
    - Servlet: AuthenticationController
    - Signup page/point of application
    - ask for details required for registeration and register user
    - currently only combination of mail id and password is used to authentication
    - Method: Get, Post
    - No login required

- #### /profile
    - Servlet: ProfileController
    - User profile view/page
    - Able to see icon, username, email
    - Redirect to endpoint
        - logout
        - edit-profile
        - updateProfile
        - deleteAccount
    - also have JS to handle profile pic updating with endpoint '/upload-profile-pic' and delete with '/delete-profile-pic'
    - Method: Get
    - Login required

- #### /logout
    - Servlet: ProfileController
    - From '/profile' endpoint
    - Send request to logout from application
    - This handled or logout used using session
    - Method: Get
    - Login required

- #### /edit-profile - @Deprecated
    - Servlet: ProfileController
    - From '/profile' endpoint
    - Edit profile view/page
    - Form to get information for updating profile
    - Method: Get
    - Login required

- #### /updateProfile - @Deprecated
    - Servlet: ProfileController
    - From '/profile' endpoint
    - Endpoint to submit form from '/edit-profile' endpoint
    - Method: POST
    - Login required

- #### /deleteAccount
    - Servlet: ProfileController
    - From '/profile' endpoint
    - To delete user
    - Method: POST
    - Login required
    - TODO:
        - Add ask confirmation before directly deleting UI
        - Remove all user assest includes pic from cloud before deleting

- #### /upload-profile-pic
    - Servlet: ProfileImageUpdaterController
    - From '/profile' and '/edit-profile' endpoint
    - To add new profile picture
    - Method: POST
    - Login required

- #### /delete-profile-pic
    - Servlet: ProfileImageUpdaterController
    - From '/profile' and '/edit-profile' endpoint
    - To remove profile picture
    - Method: POST
    - Login required

- #### / (root)
    - Servlet: TaskController
    - Task Dashboard
    - List of all user task, Summary of completion, and Add, edit, delete Task
    - Redirect to: 
        - '/profile'
    - Method: GET
    - Login Required

- #### /newTask
    - Servlet: TaskController
    - To add new task for user
    - Form get all details for task and send to backend
    - From '/ (Root)'
    - Method: POST
    - Login Required

- #### /deleteTask
    - Servlet: TaskController
    - API(js) to delete task status to completed
    - Send task id, user id fetched from session
    - From '/ (Root)'
    - Method: POST
    - Login Required

- #### /editTask
    - Servlet: TaskController
    - Get: Edit task view
    - Post: update db with changes to task
    - Send task id, user id fetched from session
    - From '/ (Root)'
    - Method: GET, POST
    - Login Required

- #### /update/username
    - Servlet: UpdateController
    - get update username request with username, fetch user id from session
    - Method: POST
    - Login Required

- #### /update/password
    - Servlet: UpdateController
    - receive old password and new password, fetch user id from session
    - Old password not match -> error: password not match & redirect to logout
    - Password check failed -> error: password not complex
    - Method: POST
    - Login Required

- #### /update/taskStatus
    - Servlet: UpdateController
    - receive task id and retrieve user id from session
    - Method: POST
    - Login Required

