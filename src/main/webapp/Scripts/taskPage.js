document.getElementById("new-task-btn").addEventListener("click", newTask)
const newTaskDialog = document.getElementById("new-task")
// const totalTask = document.getElementById("totalTask")
// const completedTask = document.getElementById("completedTask")
const myCheckbox = document.querySelectorAll(".task-checkbox")
myCheckbox.forEach(
    checkBox => {
       checkBox.addEventListener("click", (e) => {
               updateTaskStatus(e.target.value, e.target.checked)
           }
       )
}
)

function updateTaskStatus(taskId, status) {
    console.log("task: " + taskId + "; status: " + status) // TODO: write complete code to send data to backend
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
        })
            .then(res => res.json())
            .then(data => {
                if(data.status) { alert("Update success"); }
                else { alert("Update failed"); }
            })
            .catch(error => {
                console.error('Error:', error);
            });
        newTaskForm.reset()
        newTaskDialog.close()
    })
}