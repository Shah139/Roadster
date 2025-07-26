// Password visibility toggle
function togglePassword(fieldId) {
    const passwordField = document.getElementById(fieldId);
    const toggleBtn = passwordField.nextElementSibling;
    const icon = toggleBtn.querySelector('i');
    
    if (passwordField.type === 'password') {
        passwordField.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        passwordField.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}

// Show signup form
function showSignupForm() {
    const loginForm = document.getElementById('loginForm');
    const signupForm = document.getElementById('signupForm');
    
    loginForm.style.display = 'none';
    signupForm.style.display = 'block';
}

// Show login form
function showLoginForm() {
    const loginForm = document.getElementById('loginForm');
    const signupForm = document.getElementById('signupForm');
    
    signupForm.style.display = 'none';
    loginForm.style.display = 'block';
}

// Show message
function showMessage(message, type = 'success') {
    const messageDiv = document.createElement('div');
    messageDiv.textContent = message;
    messageDiv.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: ${type === 'success' ? '#10b981' : '#ef4444'};
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

// Form validation and submission
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const signupForm = document.getElementById('signupForm');
    
    // Login form submission
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        
        // Basic validation
        if (!email || !password) {
            showMessage('Please fill in all fields', 'error');
            return;
        }
        
        if (!isValidEmail(email)) {
            showMessage('Please enter a valid email address', 'error');
            return;
        }
        
        // Simulate login process
        showMessage('Signing in...', 'success');
        
        // Redirect to dashboard after 2 seconds
        setTimeout(() => {
            window.location.href = '../dashboard/index.html';
        }, 2000);
    });
    
    // Signup form submission
    signupForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const fullName = document.getElementById('fullName').value;
        const signupEmail = document.getElementById('signupEmail').value;
        const signupPassword = document.getElementById('signupPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const userRole = document.getElementById('userRole').value;
        const agreeTerms = document.getElementById('agreeTerms').checked;
        
        // Validation
        if (!fullName || !signupEmail || !signupPassword || !confirmPassword || !userRole) {
            showMessage('Please fill in all fields', 'error');
            return;
        }
        
        if (!isValidEmail(signupEmail)) {
            showMessage('Please enter a valid email address', 'error');
            return;
        }
        
        if (signupPassword.length < 6) {
            showMessage('Password must be at least 6 characters long', 'error');
            return;
        }
        
        if (signupPassword !== confirmPassword) {
            showMessage('Passwords do not match', 'error');
            return;
        }
        
        if (!agreeTerms) {
            showMessage('Please agree to the Terms of Service and Privacy Policy', 'error');
            return;
        }
        
        // Simulate signup process
        showMessage('Creating account...', 'success');
        
        // Show success message and switch to login
        setTimeout(() => {
            showMessage('Account created successfully! Please sign in.', 'success');
            showLoginForm();
            
            // Pre-fill login email
            document.getElementById('email').value = signupEmail;
        }, 2000);
    });
});

// Email validation
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
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
