import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const SOCKET_URL = 'http://localhost:8080/ws';

export let stompClient = null;

let subscription = null;

export const connectWebSocket = (chatId, onMessageReceived) => {
    const socket = new SockJS(SOCKET_URL);
    stompClient = new Client({
        webSocketFactory: () => socket,
        // debug: (str) => console.log(str),
        onConnect: () => {
            console.log('Connected to WebSocket');
            if (subscription) {
                subscription.unsubscribe(); // Unsubscribe from the previous topic
            }
            subscription = stompClient.subscribe(`/topic/global`, (message) => {
                onMessageReceived(JSON.parse(message.body));
            });
        },
        onStompError: (frame) => {
            console.error('WebSocket error:', frame);
        },
    });

    stompClient.activate();
};

export const sendMessage = (chatId, message) => {
    if (stompClient && stompClient.connected) {
        stompClient.publish({
            destination: `/app/sendMessage/${chatId}`,
            body: JSON.stringify(message),
        });
    }
};