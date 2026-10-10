let stompClient = null;
let docId = new URLSearchParams(window.location.search).get('id');
let username = "User_" + Math.floor(Math.random() * 1000);
let editor = document.getElementById('editor');
let previousContent = "";

async function init() {
    if (!docId) {
        alert("No Document ID provided!");
        window.location.href = "/";
        return;
    }

    const response = await fetch(`/api/documents/${docId}`);
    if (response.ok) {
        const doc = await response.json();
        document.getElementById('docTitle').innerText = doc.title;
        editor.value = doc.content || "";
        previousContent = editor.value;
    } else {
        alert("Document not found");
        window.location.href = "/";
        return;
    }

    connectWebSocket();
}

function connectWebSocket() {
    const socket = new SockJS('/ws-editor');
    stompClient = Stomp.over(socket);
    stompClient.debug = null;

    stompClient.connect({}, function (frame) {
        document.getElementById('status').innerText = `Status: Connected as ${username}`;
        
        stompClient.subscribe(`/topic/document/${docId}`, function (messageOutput) {
            const op = JSON.parse(messageOutput.body);
            if (op.sender !== username) {
                applyRemoteOperation(op);
            }
        });
    }, function(error) {
        document.getElementById('status').innerText = "Connection disconnected. Retrying...";
        setTimeout(connectWebSocket, 3000);
    });
}

editor.addEventListener('input', (e) => {
    const currentContent = editor.value;
    const diff = getDifference(previousContent, currentContent);
    previousContent = currentContent;

    if (diff && stompClient) {
        stompClient.send(`/app/edit/${docId}`, {}, JSON.stringify({
            docId: docId,
            sender: username,
            type: diff.type,
            charInserted: diff.text,
            position: diff.position
        }));
    }
});

function getDifference(oldStr, newStr) {
    let start = 0;
    while (start < oldStr.length && start < newStr.length && oldStr[start] === newStr[start]) {
        start++;
    }

    let oldEnd = oldStr.length - 1;
    let newEnd = newStr.length - 1;
    while (oldEnd >= start && newEnd >= start && oldStr[oldEnd] === newStr[oldEnd]) {
        oldEnd--;
        newEnd--;
    }

    if (newStr.length > oldStr.length) {
        return {
            type: 'INSERT',
            position: start,
            text: newStr.substring(start, newEnd + 1)
        };
    } else if (newStr.length < oldStr.length) {
        return {
            type: 'DELETE',
            position: start,
            text: ''
        };
    }
    return null;
}

function applyRemoteOperation(op) {
    const startPos = editor.selectionStart;
    const endPos = editor.selectionEnd;

    let content = editor.value;
    if (op.type === 'INSERT') {
        content = content.substring(0, op.position) + op.charInserted + content.substring(op.position);
    } else if (op.type === 'DELETE') {
        content = content.substring(0, op.position) + content.substring(op.position + 1);
    }

    editor.value = content;
    previousContent = content;

    let cursorOffset = 0;
    if (op.type === 'INSERT' && op.position <= startPos) {
        cursorOffset = op.charInserted.length;
    } else if (op.type === 'DELETE' && op.position < startPos) {
        cursorOffset = -1;
    }

    editor.setSelectionRange(startPos + cursorOffset, endPos + cursorOffset);
}

window.onload = init;