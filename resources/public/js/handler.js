var puresec = puresec || {};

puresec.handler = function(handler) {
    var $handler = $(handler);
    var handlerId = $handler.data("handler-id");

    var getState = function() {
        return $handler.find(".handler_state");
    };

    var isMapped = function() {
        return getState().hasClass("handler_state_mapped");
    };

    var toggleState = function() {
        getState().toggleClass("handler_state_mapped");
    };

    var remove = function() {
        $.post("/admin/remove/handler", {
            "id": handlerId
        }, function(response) {
            if (response.state == "SUCCESS") {
                $handler.remove();
            }
        });
    };

    var sendMapping = function(postUrl) {
        var url = postUrl;

        return function(detectorId) {
            $.post(url, {
                "detector_id": detectorId,
                "handler_id": handlerId
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
        "isMapped": isMapped,
        "remove": remove
    };
};
