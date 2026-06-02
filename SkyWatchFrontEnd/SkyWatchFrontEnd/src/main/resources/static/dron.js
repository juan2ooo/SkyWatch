document.addEventListener("DOMContentLoaded", function() {
    console.log("Panel de dron cargado correctamente.");
    const messageElement = document.getElementById("dron-message");
    
    if (messageElement) {
        setTimeout(() => {
            messageElement.textContent = "El dron está listo para recibir instrucciones de vuelo.";
            messageElement.style.color = "green";
        }, 2000);
    }
});
