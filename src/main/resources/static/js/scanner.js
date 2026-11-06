let html5QrCode;
function startScanner() {
    html5QrCode = new Html5Qrcode("reader");
    html5QrCode.start({ facingMode: "environment" }, { fps: 10, qrbox: 250 },
        (decodedText) => {
            document.getElementById("barcodeInput").value = decodedText;
            document.getElementById("barcodeForm").submit();
        },
        (err) => console.warn(err)
    );
}
document.addEventListener("DOMContentLoaded", startScanner);