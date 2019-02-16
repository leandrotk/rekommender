(ns nukr.interceptors.connection
  (:require [nukr.http-helpers :refer :all]))

(defn make-connection
  [connection-id first-profile-id second-profile-id]
  {:id connection-id
   :first-profile-id first-profile-id
   :second-profile-id second-profile-id})

(defn add-connection
  [database connection]
  (if-let [connections (:connections database)]
    (assoc database :connections (conj connections connection))
    (assoc database :connections [connection])))

(def connection-create
  {:name :connection-create
   :enter (fn [context]
            (if-let [first-profile-id (get-in context [:request :json-params :first-profile-id])]
              (if-let [second-profile-id (get-in context [:request :json-params :second-profile-id])]
                (let [connection-id  (str (gensym "l"))
                      new-connection (make-connection connection-id first-profile-id second-profile-id)]
                  (assoc context
                         :response (created new-connection)
                         :tx-data [add-connection new-connection])))))})