(ns combinatorics.practice.probability-statistics
  (:require [combinatorics.impl :as impl]))

(def all-poker "定义港式五张需要的整副扑克"
  [[:T :8] [:X :8] [:M :8] [:F :8]
   [:T :9] [:X :9] [:M :9] [:F :9]
   [:T :10] [:X :10] [:M :10] [:F :10]
   [:T :J] [:X :J] [:M :J] [:F :J]
   [:T :Q] [:X :Q] [:M :Q] [:F :Q]
   [:T :K] [:X :K] [:M :K] [:F :K]
   [:T :A] [:X :A] [:M :A] [:F :A]])

(def card-type "定义牌型"
  {:sequence {:name "同花顺" :value 9}
   :same4    {:name "豹子" :value 8}
   :gourd    {:name "葫芦" :value 7}
   :flush    {:name "同花" :value 6}
   :straight {:name "顺子" :value 5}
   :triple   {:name "三条" :value 4}
   :twins    {:name "两对" :value 3}
   :double   {:name "对子" :value 2}
   :single   {:name "散牌" :value 1}})

(def poker-point
  {:8  {:name "8" :value 3}
   :9  {:name "9" :value 4}
   :10 {:name "10" :value 5}
   :J  {:name "J" :value 6}
   :Q  {:name "Q" :value 7}
   :K  {:name "K" :value 8}
   :A  {:name "A" :value 9}
   :N  {:name "未知" :value 0}})

(def poker-flower
  {:T {:name "桃" :value 4}
   :X {:name "心" :value 3}
   :M {:name "梅" :value 2}
   :F {:name "方" :value 1}
   :N {:name "未知" :value 0}})

(defn card-name
  [[k v]]
  (str (-> poker-flower k :name) (-> poker-point v :name)))

(defn cards-name
  [card-seq]
  (mapv card-name card-seq))

(defn point-value
  [point]
  (-> (second point) poker-point :value))

(defn flower-value
  [flower]
  (-> (first flower) poker-flower :value))

(defn find-max
  "从一组牌中找出指定点数的最大的牌"
  [c-seq point]
  (reduce (fn [a b] (if (and (= (second a) (second b))
                             (< (flower-value a) (flower-value b)))
                      b a))
          [:N point]
          c-seq))

(defn sort-by-point
  [card-seq]
  (sort #(< (point-value %1) (point-value %2)) card-seq))

(defn type-info
  "判定牌型信息"
  [card-seq]
  (let [c-seq (vec (sort-by-point card-seq))
        v-seq (map second c-seq)
        v-map (frequencies v-seq)
        flower-same? (fn [a b] (= (first a) (first b)))
        value-inc? (fn [a b] (= 1 (- (point-value b) (point-value a))))
        sequence-trait? (fn [a b] (and (flower-same? a b) (value-inc? a b)))]
    (case (count v-map)
      5 (cond (every? #(sequence-trait? (c-seq %) (c-seq (inc %))) (range 4))
              {:type :sequence :poker (last c-seq)}
              (every? #(flower-same? (c-seq %) (c-seq (inc %))) (range 4))
              {:type :flush :poker (last c-seq)}
              (every? #(value-inc? (c-seq %) (c-seq (inc %))) (range 4))
              {:type :straight :poker (last c-seq)}
              :else {:type :single :poker (last c-seq)})
      4 {:type :double :poker (find-max card-seq (some (fn [[k v]] (when (= 2 v) k)) v-map))}
      3 (let [v-sort (sort (fn [a b] (if (= (val a) (val b))
                                       (< (:value (poker-point (key b))) (:value (poker-point (key a))))
                                       (< (val b) (val a))))
                           v-map)]
          (if (= 3 (-> v-sort first second))
            {:type :triple :poker (find-max card-seq (-> v-sort first first))}
            {:type :twins :poker [(find-max card-seq (-> v-sort first first))
                                  (find-max card-seq (-> v-sort second first))]}))
      (let [v-sort (sort (fn [a b] (< (val b) (val a))) v-map)]
        (if (= 4 (-> v-sort first second))
          {:type :same4 :poker (find-max card-seq (-> v-sort first first))}
          {:type :gourd :poker (find-max card-seq (-> v-sort first first))})))))

(defn compare-poker
  "比较两张扑克的大小"
  [a b]
  (if (= (second a) (second b))
    (< (flower-value a) (flower-value b))
    (< (point-value a) (point-value b))))

(defn compare-type-info
  "比较两组扑克的大小"
  [a-info b-info]
  (if (= (:type a-info) (:type b-info))
    (if (= :twins (:type a-info))
      (let [a-pk1 (first (:poker a-info))
            b-pk1 (first (:poker b-info))
            a-pk2 (second (:poker a-info))
            b-pk2 (second (:poker b-info))]
        (if (= (second a-pk1) (second b-pk1))
          (if (= (second a-pk2) (second b-pk2))
            (compare-poker a-pk1 b-pk1)
            (compare-poker a-pk2 b-pk2))
          (compare-poker a-pk1 b-pk1)))
      (compare-poker (:poker a-info) (:poker b-info)))
    (< (:value (card-type (:type a-info)))
       (:value (card-type (:type b-info))))))

(defn show-poker-info
  [c-seq c-info]
  (println (cards-name c-seq)
           (:name (card-type (:type c-info)))
           (if (= :twins (:type c-info))
             (cards-name (:poker c-info))
             (card-name (:poker c-info)))))

(defn count-poker
  [record seq-1 seq-2]
  (let [info-1 (type-info seq-1)
        info-2 (type-info seq-2)
        win-player (if (compare-type-info info-1 info-2) :player-2 :player-1)
        update-value (fn [n] (if n (inc n) 1))]
    (show-poker-info seq-1 info-1)
    (show-poker-info seq-2 info-2)
    (println "--------------------------- winner:" win-player)
    (-> record
        (update-in [win-player :win] update-value)
        (update-in [:player-1 :type (:type info-1)] update-value)
        (update-in [:player-2 :type (:type info-2)] update-value)
        (update :total update-value))))

(defn poker-analyze
  [a-seq b-seq]
  (let [n1 (count a-seq)
        n2 (count b-seq)
        filter-poker (fn [a b] (filter (fn [k] (not (some #(= % k) b))) a))
        remain-poker (filter-poker all-poker (concat a-seq b-seq))
        count-list (atom [])]
    (impl/combination remain-poker (- 10 n1 n2)
      (fn [c-seq] (impl/combination c-seq (- 5 n1)
                                    #(swap! count-list conj
                                            [(concat a-seq %)
                                             (concat b-seq (filter-poker c-seq %))]))))
    (reduce (fn [record [seq-1 seq-2]] (count-poker record seq-1 seq-2)) {} @count-list)))

