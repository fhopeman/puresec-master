$(document).ready(function () {

    // enable and disable the system
    $("#system_switch").click(function() {
        var $stateBtn = $(this);
        var enabled = $stateBtn.data("enabled");
        var url = "/admin/" + (enabled ? "disable" : "enable");

        $.post(url, function(response) {
            if (response.state == "SUCCESS") {
                location.reload();
            }
        });
    });

});
