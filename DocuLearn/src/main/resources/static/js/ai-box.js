document.addEventListener("DOMContentLoaded", () => {
    const avatar = document.getElementById("ai-avatar");
    const chatBox = document.getElementById("ai-chat-box");
    const sendBtn = document.querySelector(".ai-input button");

    if (avatar && chatBox) {
        avatar.addEventListener("click", () => {
            chatBox.style.display =
                chatBox.style.display === "block" ? "none" : "block";
        });
    }

    if (sendBtn) {
        sendBtn.addEventListener("click", async () => {
            const input = document.getElementById("ai-user-input");
            const msgBox = document.getElementById("ai-messages");

            if (!input.value.trim()) return;

            msgBox.innerHTML += `<div><b>Bạn:</b> ${input.value}</div>`;
            let question = input.value;
            input.value = "";

            try {
                const res = await fetch("/api/ask-ai", {
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify({question})
                });
                const data = await res.json();
                msgBox.innerHTML += `<div><b>AI:</b> ${data.answer}</div>`;
                msgBox.scrollTop = msgBox.scrollHeight;
            } catch (e) {
                msgBox.innerHTML += `<div style='color:red'><b>Lỗi:</b> Không kết nối được AI</div>`;
            }
        });
    }
});
