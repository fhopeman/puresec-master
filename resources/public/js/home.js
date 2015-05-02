$(document).ready(function () {

    // enable and disable the system
    $("#system_switch").click(function() {
        var enabled = $(this).data("enabled");

        $.post("/admin/" + enabled ? "disable" : "enable", function(response) {
            if (response.state == "SUCCESS") {
                // state switched successfully
            }
        });
    });

});
