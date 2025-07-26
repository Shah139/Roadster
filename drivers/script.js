// Search functionality
document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('searchInput');
    const driverItems = document.querySelectorAll('.driver-item');

    searchInput.addEventListener('input', function() {
        const searchTerm = this.value.toLowerCase();

        driverItems.forEach(item => {
            const driverName = item.querySelector('.driver-name').textContent.toLowerCase();
            const driverLicense = item.querySelector('.driver-license').textContent.toLowerCase();
            const driverLocation = item.querySelector('.driver-location').textContent.toLowerCase();

            if (driverName.includes(searchTerm) || 
                driverLicense.includes(searchTerm) || 
                driverLocation.includes(searchTerm)) {
                item.style.display = 'flex';
            } else {
                item.style.display = 'none';
            }
        });

        // Update stats after search
        updateStats();
    });

    function updateStats() {
        const visibleDrivers = document.querySelectorAll('.driver-item[style="display: flex"], .driver-item:not([style*="display: none"])');
        const activeDrivers = Array.from(visibleDrivers).filter(item => 
            item.querySelector('.status-badge').classList.contains('active')
        );
        const suspendedDrivers = Array.from(visibleDrivers).filter(item => 
            item.querySelector('.status-badge').classList.contains('suspended')
        );

        // Update stat numbers (only if search is active)
        if (searchInput.value.trim() !== '') {
            document.querySelector('.stat-item:nth-child(1) .stat-number').textContent = visibleDrivers.length;
            document.querySelector('.stat-item:nth-child(2) .stat-number').textContent = activeDrivers.length;
            document.querySelector('.stat-item:nth-child(3) .stat-number').textContent = suspendedDrivers.length;
        } else {
            // Reset to original numbers
            document.querySelector('.stat-item:nth-child(1) .stat-number').textContent = '156';
            document.querySelector('.stat-item:nth-child(2) .stat-number').textContent = '142';
            document.querySelector('.stat-item:nth-child(3) .stat-number').textContent = '14';
        }
    }

    // Add click functionality to driver items
    driverItems.forEach(item => {
        item.addEventListener('click', function() {
            const driverName = this.querySelector('.driver-name').textContent;
            const driverLicense = this.querySelector('.driver-license').textContent;
            
            // Here you could open a modal or navigate to driver details
            console.log(`Clicked on driver: ${driverName}, ${driverLicense}`);
            
            // For now, just highlight the clicked item
            driverItems.forEach(i => i.style.background = '#f8fafc');
            this.style.background = '#e0f2fe';
            
            setTimeout(() => {
                this.style.background = '#f8fafc';
            }, 1000);
        });
    });
});
