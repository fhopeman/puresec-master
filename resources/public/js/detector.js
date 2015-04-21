var puresec = puresec || {};

puresec.trigger = function(trigger) {
    var $trigger = $(trigger);
    var triggerId = $trigger.data("trigger-id");
    
    var toggleState = function() {
        $trigger
            .find(".trigger_state")
            .toggleClass("trigger_state_mapped");
    };
    
    var mapDetector = function(detectorId) {
        $.post("/admin/notification/map", {
            "detector_id": detectorId,
            "trigger_id": triggerId
        }, function(response) {
            if (response.state == "SUCCESS") {
                toggleState();
            }
        });
    };
    
    return {
        "mapDetector": mapDetector
    };
};
