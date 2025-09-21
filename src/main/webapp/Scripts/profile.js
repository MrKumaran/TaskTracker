import { showNotification } from './sideNotification.js'

const profilePicDiv = document.getElementById("profilePicDiv")
const picOptions = document.getElementById("picOptions")
const viewPicBtn = document.getElementById("viewPicBtn")
const uploadPicBtn = document.getElementById("uploadPicBtn")
const uploadInput = document.getElementById("uploadInput")

// Toggle options menu on image click
profilePicDiv.addEventListener("click", (e) => {
    picOptions.style.display = picOptions.style.display === "grid" ? "none" : "grid"
})

// Close menu if clicked outside
document.addEventListener("click", (e) => {
    if (!profilePicDiv.contains(e.target) && !picOptions.contains(e.target)) {
        picOptions.style.display = "none"
    }
})

// View image
viewPicBtn.addEventListener("click", () => {
    const img = profilePicDiv.querySelector("img")
    if (img && img.src) {
        window.open(img.src, "_blank")
    } else {
        showNotification("No profile image", "false")
    }
})

// Upload new image
uploadPicBtn.addEventListener("click", () => {
    uploadInput.click()
})

// Handle file selection and sending to backend getting response and notifying user
uploadInput.addEventListener("change", (e) => {
    const file = e.target.files[0]
    if (file) {
        const formData = new FormData()
        const reader = new FileReader()
        formData.append('profilePic', file)
        fetch('/api/upload-image', {
            method: 'POST',
            body: formData,
        })
            .then(response => response.json())
            .then(data => {
                profilePicDiv.innerHTML = '<img src="' + data.fileName + '" alt="" id="profilePic"/>'
                showNotification("Profile picture updated", "true")
            })
            .catch(error => {
                showNotification("Error uploading image", "false")
            })
    }
})
