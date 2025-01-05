// Modified script.js
document.addEventListener('DOMContentLoaded', function() {
    const indicatorDot = document.querySelector('.indicator-dot');
    const indicatorText = document.querySelector('.indicator-text');
    const lastCommandSpan = document.getElementById('last-command');
    const commandIndicator = document.querySelector('.command-indicator');
    
    function updateConnectionStatus(isConnected) {
        if (isConnected) {
            indicatorDot.classList.add('connected');
            indicatorText.textContent = 'Connected';
        } else {
            indicatorDot.classList.remove('connected');
            indicatorText.textContent = 'Disconnected';
        }
    }

    function checkConnection() {
        fetch('/status')
            .then(response => {
                updateConnectionStatus(response.ok);
            })
            .catch(() => {
                updateConnectionStatus(false);
            });
    }

    setInterval(checkConnection, 100);
    checkConnection();

    document.querySelectorAll('button').forEach(button => {
        button.addEventListener('click', function() {
            const commandId = this.id;
            fetch(`/command?id=${commandId}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    // Update command display
                    lastCommandSpan.textContent = commandId;
                    commandIndicator.classList.add('show');
                    
                    // Hide command after 1 second
                    setTimeout(() => {
                        commandIndicator.classList.remove('show');
                    }, 1000);
                    
                    // Add visual feedback
                    this.classList.add('active');
                    setTimeout(() => this.classList.remove('active'), 200);
                })
                .catch(error => {
                    console.error(error);
                    lastCommandSpan.textContent = 'Error';
                    commandIndicator.classList.add('show');
                    setTimeout(() => {
                        commandIndicator.classList.remove('show');
                    }, 1000);
                });
        });
    });
});