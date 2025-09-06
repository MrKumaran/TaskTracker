document.addEventListener('DOMContentLoaded',
    ()=>{
        const newTaskBtn = document.getElementById("new-task-btn")
        const newTaskDialog = document.getElementById("new-task")
        newTaskBtn.addEventListener("click", newTask)
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
                console.log(document.getElementById("new-task-title").innerText)
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
    }
)

