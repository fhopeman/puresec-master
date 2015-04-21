$(document).ready(function () {

    // map trigger
    $(".trigger").click(function() {
        var $trigger = $(this);
        var detectorId = $trigger.closest(".detector").data("detector-id");
        var triggerId = $trigger.data("trigger-id");

        $.post("/admin/notification/map", {
            "detector_id": detectorId,
            "trigger_id": triggerId
        }, function(response) {
            if (response.state == "SUCCESS") {
                $trigger.find(".trigger_state").toggleClass("trigger_state_mapped");
            }
        });
    });

});
