document.getElementById("new-task-btn").addEventListener("click", newTask)
const newTaskDialog = document.getElementById("new-task")
const myCheckbox = document.querySelectorAll(".task-checkbox")
const deleteTaskVector = document.querySelectorAll(".deleteTask")
const outsideEvent = document.getElementById("new-task")

// attaching event listeners
outsideEvent.addEventListener("click", (e) => {
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

deleteTaskVector.forEach(
    edit => {
        edit.addEventListener("click", (e) => {
                deleteTask(e)
            }
        )
    }
    )

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
    } ).catch(error => {
            alert('Error occurred:' + error)
        })
}

function updateTaskStatus(e) {
    const taskId = e.target.value
    const status = e.target.checked
    fetch('/update/taskStatus', {
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
