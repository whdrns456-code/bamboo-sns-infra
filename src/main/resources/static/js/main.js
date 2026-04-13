// --- 1. 배경 애니메이션 (Matrix Bamboo) ---
const canvas = document.getElementById('bambooCanvas');
if (canvas) {
    const ctx = canvas.getContext('2d');
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    const bambooChars = '│┃╽╿╏║╎╟╢';
    const fontSize = 16;
    const columns = canvas.width / fontSize;
    const drops = Array(Math.floor(columns)).fill(1);

    function draw() {
        ctx.fillStyle = 'rgba(10, 14, 23, 0.1)';
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        ctx.font = fontSize + 'px monospace';

        for (let i = 0; i < drops.length; i++) {
            const text = bambooChars.charAt(Math.floor(Math.random() * bambooChars.length));
            const alpha = Math.random() > 0.95 ? 1 : 0.5;
            ctx.fillStyle = `rgba(0, 255, 65, ${alpha})`;
            ctx.fillText(text, i * fontSize, drops[i] * fontSize);

            if (Math.random() > 0.98) {
                ctx.fillStyle = '#00ff41';
                ctx.fillText('━', i * fontSize, drops[i] * fontSize);
            }
            if (drops[i] * fontSize > canvas.height && Math.random() > 0.975) {
                drops[i] = 0;
            }
            drops[i]++;
        }
    }
    setInterval(draw, 33);
    window.addEventListener('resize', () => {
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;
    });
}

// --- 2. 시스템 모달 (공통 기능) ---
window.showSystemMessage = function(message, isError = false) {
    const modal = document.getElementById('systemModal');
    const title = document.getElementById('modalTitle');
    const msgArea = document.getElementById('modalMessage');
    if (modal && title && msgArea) {
        title.innerText = isError ? '[SYSTEM_CRITICAL_ERROR]' : '[SYSTEM_NOTIFICATION]';
        title.style.color = isError ? '#ff4d4d' : 'var(--primary-green)';
        msgArea.innerText = `> ${message}`;
        modal.style.display = 'flex';
    }
};

window.closeModal = function() {
    const modal = document.getElementById('systemModal');
    if (modal) modal.style.display = 'none';
};