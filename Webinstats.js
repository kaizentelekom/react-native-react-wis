import { NativeModules,NativeEventEmitter,AppState,Platform } from 'react-native';
const { ReactWis } = NativeModules;
const wisEvent = new NativeEventEmitter(NativeModules.ReactWis);

const Webinstats = {};
Webinstats._execute = function(map){
    try{
        ReactWis.execute(map);
    }catch (err){
        console.log('webinstats_log : ReactWIS : Exception : _execute : ',err);
    }
};
Webinstats.setInlineCallback = function(companyId,elementId,callback){
    ReactWis.setInlineCallback(companyId,function(html,json){
        console.log('inline callback triggered in wis js');
        try{
            console.log('inline 1');
            let data = JSON.parse(json);
            console.log('inline 2');
            if(data.slc && data.slc != '' && data.slc == elementId){
                console.log('inline 3');
                callback(html,json)
                document.getElementById(elementId).hidden = false;
                console.log('inline 4');
            }
        }catch (e){
            console.log('inline 5',e);
        }
    });
};
Webinstats.saveTestParameters = function(companyId,url){
    try{
        ReactWis.saveTestParameters(companyId,url);
    }catch (e){

    }

};
Webinstats.setClickCallback = function(companyId,successCallback){
    try{
        wisEvent.addListener('pushClicked', data => {
            successCallback(data);
            console.log('webinstats_log : ReactWIS : ',Platform.OS,' event triggered with : ', data);
        });
        if (Platform.OS == 'android'){
            AppState.addEventListener('change',function(){
                console.log('webinstats_log : ReactWIS : ',Platform.OS,' changed triggered with : ');
                ReactWis.setPushClickCallback(companyId,function(data){
                    successCallback(data);
                    console.log('webinstats_log : ReactWIS : android callback triggered with : ', data);
                });
            });

        }else if (Platform.OS == 'ios'){
            ReactWis.setPushClickCallback(companyId, function (data) {
                successCallback(data);
                console.log('webinstats_log : ReactWIS : ios callback triggered with : ', data);
            });
        }

    }catch (err){
        console.log('webinstats_log : ReactWIS : Exception : _setClickCallback : ',err);
    }

};


Webinstats.addItem = function(companyId,domain,productId,quantity,price,category,title){
    try{
        ReactWis.addItem(companyId,domain,productId,quantity,price,category,title);
    }catch (err){
        console.log('webinstats_log : ReactWIS : Exception : addItem : ',err);
    }
};

export default Webinstats;


