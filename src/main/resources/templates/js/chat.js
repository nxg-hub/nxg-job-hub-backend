const socket = new SockJS('/chat');
const stompClient = Stomp.over(socket);

const messageInput = document.getElementById('message-input');
const sendButton = document.getElementById('send-button');
const chatMessages = document.getElementById('chat-messages');
const username = prompt('Enter your username:');

stompClient.connect({}, () => {
    stompClient.subscribe('/chatroom/public', (message) => {
        showMessage(JSON.parse(message.body));
    });

    sendButton.addEventListener('click', () => {
        const content = messageInput.value.trim();
        if (content) {
            sendMessage({ content, sender: username, receiver: 'public' });
        }
    });

    // Add private messaging support
    const privateMessageButton = document.getElementById('private-message-button');
    const privateMessageInput = document.getElementById('private-message-input');
    const recipientInput = document.getElementById('recipient-input');

    privateMessageButton.addEventListener('click', () => {
        const content = privateMessageInput.value.trim();
        const recipient = recipientInput.value.trim();
        if (content && recipient) {
            sendPrivateMessage({ content, sender: username, receiver: recipient });
        }
    });

    // Subscribe to private messages
    stompClient.subscribe('/user/queue/direct', (message) => {
        showMessage(JSON.parse(message.body));
    });
});

function sendMessage(message) {
    stompClient.send('/app/chatroom/message', {}, JSON.stringify(message));
}

function sendPrivateMessage(message) {
    stompClient.send('/app/direct-message', {}, JSON.stringify(message));
}

function showMessage(message) {
    const messageElement = document.createElement('div');
    messageElement.textContent = `${message.sender}: ${message.content}`;
    chatMessages.appendChild(messageElement);
}
