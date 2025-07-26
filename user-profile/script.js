// Password visibility toggle
function togglePassword(fieldId) {
    const passwordField = document.getElementById(fieldId);
    const toggleBtn = passwordField.nextElementSibling.querySelector('i');
    
    if (passwordField.type === 'password') {
        passwordField.type = 'text';
        toggleBtn.classList.remove('fa-eye');
        toggleBtn.classList.add('fa-eye-slash');
    } else {
        passwordField.type = 'password';
        toggleBtn.classList.remove('fa-eye-slash');
        toggleBtn.classList.add('fa-eye');
    }
}

// Toggle driver section based on user role
function toggleDriverSection() {
    const userRole = document.getElementById('userRole').value;
    const driverSection = document.getElementById('driverSection');
    
    if (userRole === 'driver') {
        driverSection.style.display = 'block';
    } else {
        driverSection.style.display = 'none';
    }
}

// Driver status toggle
function setDriverStatus(status) {
    const freeBtn = document.querySelector('.status-btn.free');
    const occupiedBtn = document.querySelector('.status-btn.occupied');
    
    // Remove active class from both buttons
    freeBtn.classList.remove('active');
    occupiedBtn.classList.remove('active');
    
    // Add active class to selected button
    if (status === 'free') {
        freeBtn.classList.add('active');
    } else if (status === 'occupied') {
        occupiedBtn.classList.add('active');
    }
    
    // Show success message
    showStatusMessage(`Driver status changed to ${status}`);
    
    // Update status info
    updateStatusInfo(status);
}

// Show status message
function showStatusMessage(message) {
    // Create a temporary message element
    const messageDiv = document.createElement('div');
    messageDiv.textContent = message;
    messageDiv.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: #10b981;
        color: white;
        padding: 12px 20px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        z-index: 1000;
        animation: slideIn 0.3s ease;
    `;
    
    document.body.appendChild(messageDiv);
    
    // Remove message after 3 seconds
    setTimeout(() => {
        messageDiv.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => {
            document.body.removeChild(messageDiv);
        }, 300);
    }, 3000);
}

// Update status info
function updateStatusInfo(status) {
    const statusItems = document.querySelectorAll('.status-item');
    const timeItem = statusItems[0].querySelector('span');
    
    timeItem.textContent = 'Status changed: Just now';
    
    // You could also update location based on status
    if (status === 'occupied') {
        const locationItem = statusItems[1].querySelector('span');
        locationItem.textContent = 'Current location: En route to destination';
    }
}

// Password validation
document.addEventListener('DOMContentLoaded', function() {
    // Initialize driver section visibility based on current role
    toggleDriverSection();
    
    const changePasswordBtn = document.querySelector('.change-password-btn');
    const currentPassword = document.getElementById('currentPassword');
    const newPassword = document.getElementById('newPassword');
    const confirmPassword = document.getElementById('confirmPassword');
    
    changePasswordBtn.addEventListener('click', function() {
        // Basic validation
        if (!currentPassword.value) {
            showErrorMessage('Please enter your current password');
            return;
        }
        
        if (!newPassword.value) {
            showErrorMessage('Please enter a new password');
            return;
        }
        
        if (newPassword.value.length < 6) {
            showErrorMessage('New password must be at least 6 characters long');
            return;
        }
        
        if (newPassword.value !== confirmPassword.value) {
            showErrorMessage('New passwords do not match');
            return;
        }
        
        // If validation passes
        showStatusMessage('Password updated successfully');
        
        // Clear password fields
        currentPassword.value = '';
        newPassword.value = '';
        confirmPassword.value = '';
    });
    
    // Save changes button
    const saveBtn = document.querySelector('.action-btn');
    saveBtn.addEventListener('click', function() {
        showStatusMessage('Profile changes saved successfully');
    });
});

// Show error message
function showErrorMessage(message) {
    const messageDiv = document.createElement('div');
    messageDiv.textContent = message;
    messageDiv.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: #ef4444;
        color: white;
        padding: 12px 20px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        z-index: 1000;
        animation: slideIn 0.3s ease;
    `;
    
    document.body.appendChild(messageDiv);
    
    setTimeout(() => {
        messageDiv.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => {
            document.body.removeChild(messageDiv);
        }, 300);
    }, 3000);
}

// Add CSS animations
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);
