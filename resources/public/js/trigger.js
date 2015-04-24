var puresec = puresec || {};

puresec.trigger = function(trigger) {
    var $trigger = $(trigger);
    var triggerId = $trigger.data("trigger-id");

    var getState = function() {
        return $trigger.find(".trigger_state");
    };

    var isMapped = function() {
        return getState().hasClass("trigger_state_mapped");
    };

    var toggleState = function() {
        getState().toggleClass("trigger_state_mapped");
    };

    var sendMapping = function(postUrl) {
        var url = postUrl;

        return function(detectorId) {
            $.post(url, {
                "detector_id": detectorId,
                "trigger_id": triggerId
            }, function (response) {
                if (response.state == "SUCCESS") {
                    toggleState();
                }
            });
        };
    };

    return {
        "mapDetector": sendMapping("/admin/notification/map"),
        "unmapDetector": sendMapping("/admin/notification/unmap"),
        "isMapped": isMapped
    };
};
