import { showNotification } from './sideNotification.js'

document.getElementById("new-task-btn").addEventListener("click", newTask)
const newTaskDialog = document.getElementById("new-task")
const myCheckbox = document.querySelectorAll(".task-checkbox")
const editTaskVector = document.querySelectorAll(".editTask")
const deleteTaskVector = document.querySelectorAll(".deleteTask")
let update = document.getElementById("sideNotification").innerText
const outsideEvent = document.getElementById("new-task")
update = update.split(",")

// attaching event listeners
outsideEvent.addEventListener("click", (e) => {
    console.log("Dialog outside click")
    if(e.target === outsideEvent) {
        newTaskDialog.close()
    }
})

myCheckbox.forEach(
    checkBox => {
        checkBox.addEventListener("click", (e) => {
                updateTaskStatus(e)
            }
        )
    }
    )

editTaskVector.forEach(
    edit => {
        edit.addEventListener("click", (e) => {
                editTask(e)
            }
        )
    }
    )

deleteTaskVector.forEach(
    edit => {
        edit.addEventListener("click", (e) => {
                deleteTask(e)
            }
        )
    }
    )

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
}

if(update[0] !== "greets" && update[0] !== "null") {
    showNotification(msg, update[1])
}

function editTask(e) {
    const taskId = e.currentTarget.getAttribute("id")
    console.log("Edit Task Id: " + taskId)
}

function deleteTask(e) {
    const taskId = e.currentTarget.getAttribute("id")
    fetch('/deleteTask', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: 'taskId=' + encodeURIComponent(taskId)
        }
    ).then(res => {
        if (res.ok) {
            window.location.href = "/"
        } else {
            alert("Deleting task Failed, Try again :(")
            window.location.href = "/"
        }
    })
        .catch(error => {
            alert('Error occurred:' + error)
        })
}

function updateTaskStatus(e) {
    const taskId = e.target.value
    const status = e.target.checked
    fetch('/updateTaskStatus', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: 'taskId=' + encodeURIComponent(taskId) + '&isDone=' + encodeURIComponent(status)
        }
    ).then(res => {
        if (res.ok) {
            window.location.href = "/"
        } else {
            alert("Updating task status Failed, Try again :(")
            window.location.href = "/"
        }
    })
        .catch(error => {
            alert('Error occurred:' + error)
        })
}

function newTask() {
    newTaskDialog.showModal()
    const newTaskForm = document.getElementById("new-task-form")
    const newTaskSubmitBtn = document.getElementById("task-submit-btn")
    newTaskSubmitBtn.addEventListener("click", (e) => {
        e.preventDefault();
        //   Converting to json for sending data to backend
        const payload = {
            "new-task-title": document.getElementById("new-task-title").value,
            "new-task-due": document.getElementById("new-task-due").value
        }
        console.log(payload)
        fetch('/newTask', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        }).then(res => {
            if (res.ok) {
                window.location.href = "/"
            } else {
                alert("Adding new task Failed, Try again :(")
                window.location.href = "/"
            }
        })
            .catch(error => {
                alert('Error occurred:' + error)
            });
        newTaskForm.reset()
        newTaskDialog.close()
    })
}
