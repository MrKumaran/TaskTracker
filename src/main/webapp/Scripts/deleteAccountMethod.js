const deleteAccBTN = document.getElementById("accountDeleteBTN")
deleteAccBTN.addEventListener('click', deleteAccount)
const currPsswrd = document.getElementById("current-password")
// method just to send it as post request
function deleteAccount() {
    fetch('/deleteAccount', {
            method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
            body: JSON.stringify({'current-password': currPsswrd.value})
        })
        .then(() => {
               window.location.href = "/landing"
        }
        )
        .catch((reason) => {
            console.log(reason)
        })
}