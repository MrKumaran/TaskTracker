import {showNotification} from "./sideNotification.js";

let update = document.getElementById("sideNotification").innerText
update = update.split(",")

let msg
switch (update[0]) {
    case "greets":
        showNotification(`Welcome! ${update[1]}`, "true")
        break
    case "newTaskAdded":
        msg = (update[1] === "true")? "New task added": "New task didn't added"
        break
    case "updatedTaskStatus":
        msg = (update[1] === "true")? "Task status updated": "Task status didn't updated"
        break
    case "deletedTask":
        msg = (update[1] === "true")? "Task deleted": "Task didn't deleted"
        break
    case "profileUpdated":
        msg = (update[1] === "true")? "Profile details updated":"Profile didn't updated "
        break
    case "DataFetch":
        msg = "Error fetching data Refresh"
        break
    case "taskUpdate":
        msg = (update[1] === "true")? "Task successfully Updated":"Task didn't updated "
        break
    case "profileUpdated:password":
        msg = "Profile didn't updated:\n Password isn't complex Enough"
        break
    case "TASK_NOT_FOUND":
        msg = "Task not found, Try reloading or relogging in"
        break;
}

if(update[0] !== "greets" && update[0] !== "null") {
    showNotification(msg, update[1])
}