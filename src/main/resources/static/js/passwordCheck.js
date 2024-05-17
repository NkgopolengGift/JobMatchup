document.addEventListener('DOMContentLoaded', function() {
    document.querySelector('form').onsubmit = function() {
        return validateForm();
    };
});

function validateForm() {
    const password = document.getElementById('password').value;
    const confirm = document.getElementById('confirm').value;
    const errorDiv = document.getElementById('error');
    const messageDiv = document.getElementById('message');
    
    // Clear previous messages
    errorDiv.style.display = 'none';
    messageDiv.style.display = 'none';
    
    const errors = [];
    
    // Check password length
    if (password.length < 8) {
        errors.push("Password must be at least 8 characters long.");
    }
    // Check for uppercase letter
    if (!/[A-Z]/.test(password)) {
        errors.push("Password must contain at least one uppercase letter.");
    }
    // Check for lowercase letter
    if (!/[a-z]/.test(password)) {
        errors.push("Password must contain at least one lowercase letter.");
    }
    // Check for digit
    if (!/[0-9]/.test(password)) {
        errors.push("Password must contain at least one digit.");
    }
    // Check for special character
    if (!/[^A-Za-z0-9]/.test(password)) {
        errors.push("Password must contain at least one special character.");
    }
    // Check if passwords match
    if (password !== confirm) {
        errors.push("Passwords do not match.");
    }

    if (errors.length > 0) {
        errorDiv.innerHTML = errors.join("<br>");
        errorDiv.style.display = 'block';
        return false;
    }
    
    messageDiv.innerHTML = "Form submitted successfully!";
    messageDiv.style.display = 'block';
    return true;
}
