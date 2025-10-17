const newTaskForm = document.getElementById("edit-task-form")
const newTaskSubmitBtn = document.getElementById("task-submit-btn")
newTaskSubmitBtn.addEventListener("click", (e) => {
    e.preventDefault();
    //   Converting to json for sending data to backend
    const payload = {
        "taskId": document.getElementById("taskId").innerText,
        "new-task-title": document.getElementById("new-task-title").value,
        "new-task-due": document.getElementById("new-task-due").value
    }
    fetch('/editTask', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    }).then(res => {
        if (res.ok) {
            window.location.href = "/"
        } else {
            alert("Updating task Failed, Try again :(")
            window.location.href = "/"
        }
    })
        .catch(error => {
            alert('Error occurred:' + error)
        });
    newTaskForm.reset()
})